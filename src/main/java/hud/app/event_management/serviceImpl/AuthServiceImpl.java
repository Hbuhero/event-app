package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.*;
import hud.app.event_management.dto.response.LoginResponseDto;
import hud.app.event_management.jwt.JwtService;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.repository.UserAccountRepository;
import hud.app.event_management.service.AuthService;
import hud.app.event_management.service.UserAccountService;
import hud.app.event_management.userDetailService.UserDetailsImpl;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.ResponseCode;
import hud.app.event_management.utils.userExtractor.LoggedUser;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {
    private final int duration = 600;
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final LoggedUser loggedUser;

    @Autowired
    public AuthServiceImpl(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, LoggedUser loggedUser){
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.loggedUser = loggedUser;
    }
    @Override
    public Response<String> register(UserAccountDto userAccountDto) {
        try {
            // todo: validate null values
            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(userAccountDto.getEmail());
            // todo: validate email
            if(optionalUserAccount.isPresent()){
                return new Response<>(true, "Email already exists", ResponseCode.DUPLICATE_EMAIL);
            }
            // todo: validate phone number
            Optional<UserAccount> accountOptional = userAccountRepository.findFirstByPhone(userAccountDto.getPhone());
            if (accountOptional.isPresent()){
                return new Response<>(true, "Phone number already exists", ResponseCode.DUPLICATE);
            }

            UserAccount userAccount = UserAccount.builder()
                    .firstName(userAccountDto.getFirstname())
                    .middleName(userAccountDto.getMiddleName())
                    .lastName(userAccountDto.getLastname())
                    .username(userAccountDto.getEmail())
                    .phone(userAccountDto.getPhone())
                    .password(passwordEncoder.encode(userAccountDto.getPassword()))
                    .address(userAccountDto.getAddress())
                    .userType("USER")
                    .enabled(false)
                    .build();

            Random random = new SecureRandom();
            int nextInt = random.nextInt(100001, 999999);

            userAccount.setOneTimePassword(String.valueOf(nextInt));
            // todo: implement email sender
            userAccount.setLastOtpSentAt(LocalDateTime.now());

            userAccountRepository.save(userAccount);

            return new Response<>(false, "Registration successful: ", ResponseCode.SUCCESS, String.valueOf(nextInt));

        } catch (Exception e) {
            return new Response<>(true, "Registration failed with cause: "+ e.getMessage(), ResponseCode.FAIL);
        }

    }

    @Override
    public Response<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // todo: generate jwt and refresh token

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(loginRequestDto.getUsername());
            if (optionalUserAccount.isEmpty()){
                return new Response<>(true, "Login failed", ResponseCode.FAIL);
            }
            UserAccount userAccount = optionalUserAccount.get();
            String jwt = jwtService.generateToken(UserDetailsImpl.build(userAccount));

            return new Response<>(false, "Login successful", ResponseCode.SUCCESS, new LoginResponseDto(jwt, userAccount.getUsername(), userAccount.getUserType()));
        } catch (Exception e) {
            return new Response<>(true, "Failed to login with cause: "+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<String> validateOTP(OTPRequestDto otpRequestDto) {
        try {
            // todo: validate null values
            if (otpRequestDto.getOtp().isBlank() || otpRequestDto.getUsername().isBlank()){
                return new Response<>(true, "Username or OTP must not be empty", ResponseCode.NULL_ARGUMENT);
            }

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(otpRequestDto.getUsername());
            if (optionalUserAccount.isEmpty()){
                return new Response<>(true, "User not found", ResponseCode.USER_NOT_FOUND);
            }

            UserAccount userAccount = optionalUserAccount.get();

            if (userAccount.isOtpVerified()){
                return new Response<>(true, "Code is already verified", ResponseCode.FAIL);
            }
            // is the otp expired
            Duration difference = Duration.between(userAccount.getLastOtpSentAt(), LocalDateTime.now());
            if (difference.getSeconds() >= duration){
                return new Response<>(true, "OTP is has expired, request new OTP", ResponseCode.FAIL);
            }
            // is the otp valid
            if (!otpRequestDto.getOtp().equals(userAccount.getOneTimePassword())){
                return new Response<>(true, "Invalid or wrong OTP", ResponseCode.FAIL);
            }

            userAccount.setOtpVerified(true);
            userAccountRepository.save(userAccount);
            return new Response<>(false, "OTP validation successful", ResponseCode.SUCCESS);
        } catch (Exception e) {
            return new Response<>(true, "Failed to validate OTP with cause: "+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<String> forgetPassword(String username) {
        try{
            // find user
            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(username);
            if (optionalUserAccount.isEmpty()){
                return new Response<>(true, "User not found", ResponseCode.USER_NOT_FOUND);
            }

            UserAccount userAccount = optionalUserAccount.get();
            // create otp
            Random random = new SecureRandom();
            int nextInt = random.nextInt(100001, 999999);

            userAccount.setOneTimePassword(String.valueOf(nextInt));
            userAccount.setOtpVerified(false);
            // todo: send email with otp
            userAccount.setLastOtpSentAt(LocalDateTime.now());

            userAccountRepository.save(userAccount);

            return new Response<>(false, "Forget password initiated successfully: ", ResponseCode.SUCCESS, String.valueOf(nextInt));
        } catch (Exception e) {
            return new Response<>(true, "Failed to initiate forget password with cause: "+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<String> resetPassword(PasswordRequestDto passwordRequestDto) {
        try {
            if (passwordRequestDto.getPassword().isBlank() || passwordRequestDto.getUsername().isBlank()){
                return new Response<>(true, "Username and password must not be null", ResponseCode.NULL_ARGUMENT);
            }

            // todo: validate email

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(passwordRequestDto.getUsername());

            if (optionalUserAccount.isEmpty()){
                return new Response<>(true, "User not found", ResponseCode.USER_NOT_FOUND);
            }

            UserAccount userAccount = optionalUserAccount.get();

            if (!userAccount.isOtpVerified()){
                return new Response<>(true, "Code is not verified", ResponseCode.FAIL);
            }

            userAccount.setPassword(passwordEncoder.encode(passwordRequestDto.getPassword()));
            userAccountRepository.save(userAccount);
            return new Response<>(false, "Password reset successfully", ResponseCode.SUCCESS);
        } catch (Exception e) {
            return new Response<>(true, "Failed to reset password with cause: "+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<UserAccount> activateAccount(ActivationRequestDTO activationRequestDTO) {
        try{
            if (activationRequestDTO.getUsername().isBlank() || activationRequestDTO.getOpt().isBlank()){
                return new Response<>(true, "Username and OTP cant not be null", ResponseCode.FAIL);
            }

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(activationRequestDTO.getUsername());
            if (optionalUserAccount.isEmpty()){
                return new Response<>(true, "User not found", ResponseCode.FAIL);
            }
            UserAccount userAccount = optionalUserAccount.get();

            if (userAccount.isOtpVerified()){
                return new Response<>(true, "Code is already verified", ResponseCode.FAIL);
            }

            Duration difference = Duration.between(userAccount.getLastOtpSentAt(), LocalDateTime.now());
            if(difference.getSeconds() >= duration){
              return new Response<>(true, "Invalid OTP, code already expired", ResponseCode.FAIL);
            }

            if (!activationRequestDTO.getOpt().equalsIgnoreCase(userAccount.getOneTimePassword())){
                return new Response<>(true, "OTP is invalid, please try again", ResponseCode.FAIL);
            }

            userAccount.setEnabled(true);
            userAccount.setAccountNonLocked(true);
            userAccount.setAccountNonExpired(true);
            userAccount.setCredentialsNonExpired(true);
            UserAccount savedUserAccount = userAccountRepository.save(userAccount);
            return new Response<>(false, "Account activation successful", ResponseCode.SUCCESS, savedUserAccount);
        } catch (Exception e) {
            return new Response<>(true, "Failed to activate account with cause: "+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<String> resendOTP(String username) {
        try{
            if (username.isBlank()){
                return new Response<>(true, "Username cant be null", ResponseCode.FAIL);
            }

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(username);
            if (optionalUserAccount.isEmpty()){
                return new Response<>(true, "User not found", ResponseCode.FAIL);
            }
            UserAccount userAccount = optionalUserAccount.get();

            if (userAccount.isOtpVerified()){
                return new Response<>(true, "Code is already verified", ResponseCode.FAIL);
            }

            Duration difference = Duration.between(userAccount.getLastOtpSentAt(), LocalDateTime.now());
            if(difference.getSeconds() <= duration){
                return new Response<>(true, "OTP code is still active", ResponseCode.FAIL);
            }

            Random random = new SecureRandom();
            int nextInt = random.nextInt(100001, 999999);

            userAccount.setOneTimePassword(String.valueOf(nextInt));
            // todo: send email with otp
            userAccount.setLastOtpSentAt(LocalDateTime.now());
            userAccountRepository.save(userAccount);

            return new Response<>(false, "OTP sent successfully", ResponseCode.SUCCESS, String.valueOf(nextInt));
        } catch (Exception e) {
            return new Response<>(true, "Failed to resend code with cause: "+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<UserAccount> getLoggedUser() {
        try {
            UserAccount userAccount = loggedUser.getUser();

            if (userAccount == null){
                return new Response<>(true, "Unauthorized anonymous user", ResponseCode.UNAUTHORIZED);
            }

            return new Response<>(false, ResponseCode.SUCCESS, userAccount);
        } catch (Exception e) {
            return new Response<>(true, "Operation unsuccessful", ResponseCode.FAIL);
        }

    }

    // todo: logout endpoint
}

// resend, validate, activate
