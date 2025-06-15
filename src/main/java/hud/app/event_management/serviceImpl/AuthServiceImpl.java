package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.*;
import hud.app.event_management.dto.response.LoginResponseDto;
import hud.app.event_management.security.jwt.JwtService;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.repository.UserAccountRepository;
import hud.app.event_management.service.AuthService;
import hud.app.event_management.userDetailService.UserDetailsImpl;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.ResponseCode;
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



    @Autowired
    public AuthServiceImpl(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService){
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;

    }
    @Override
    public Response<String> register(UserAccountRegistrationRequest userAccountDto) {
        try {
            // todo: validate null values
            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsernameOrPhone(userAccountDto.getEmail(), userAccountDto.getPhone());
            // todo: validate email
            if(optionalUserAccount.isPresent()){
                return new Response<>(true, "Email or phone number already exists", ResponseCode.DUPLICATE_EMAIL);
            }
            // todo: validate phone number
//            Optional<UserAccount> accountOptional = userAccountRepository.findFirstByPhone(userAccountDto.getPhone());
//            if (accountOptional.isPresent()){
//                return new Response<>(true, "Phone number already exists", ResponseCode.DUPLICATE);
//            }

            UserAccount userAccount = UserAccount.builder()
                    .firstName(userAccountDto.getFirstname())
                    .middleName(userAccountDto.getMiddleName())
                    .lastName(userAccountDto.getLastname())
                    .username(userAccountDto.getEmail())
                    .phone(userAccountDto.getPhone())
                    .password(passwordEncoder.encode(userAccountDto.getPassword()))
                    .address(userAccountDto.getAddress())
                    .profilePhoto(userAccountDto.getProfilePhoto())
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
    public Response<LoginResponseDto> login(LoginRequest loginRequest) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // todo: generate jwt and refresh token

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(loginRequest.getUsername());
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
    public Response<String> validateOTP(OTPVerificationRequest otpVerificationRequest) {
        try {
            // todo: validate null values
            if (otpVerificationRequest.getOtp().isBlank() || otpVerificationRequest.getUsername().isBlank()){
                return new Response<>(true, "Username or OTP must not be empty", ResponseCode.NULL_ARGUMENT);
            }

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(otpVerificationRequest.getUsername());
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
            if (!otpVerificationRequest.getOtp().equals(userAccount.getOneTimePassword())){
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
    public Response<String> resetPassword(PasswordResetRequest passwordResetRequestDto) {
        try {
            if (passwordResetRequestDto.getPassword().isBlank() || passwordResetRequestDto.getUsername().isBlank()){
                return new Response<>(true, "Username and password must not be null", ResponseCode.NULL_ARGUMENT);
            }

            // todo: validate email

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(passwordResetRequestDto.getUsername());

            if (optionalUserAccount.isEmpty()){
                return new Response<>(true, "User not found", ResponseCode.USER_NOT_FOUND);
            }

            UserAccount userAccount = optionalUserAccount.get();

            if (!userAccount.isOtpVerified()){
                return new Response<>(true, "Code is not verified", ResponseCode.FAIL);
            }

            userAccount.setPassword(passwordEncoder.encode(passwordResetRequestDto.getPassword()));
            userAccountRepository.save(userAccount);
            return new Response<>(false, "Password reset successfully", ResponseCode.SUCCESS);
        } catch (Exception e) {
            return new Response<>(true, "Failed to reset password with cause: "+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<UserAccount> activateAccount(ActivationRequest activationRequest) {
        try{
            if (activationRequest.getUsername().isBlank() || activationRequest.getOtp().isBlank()){
                return new Response<>(true, "Username and OTP cant not be null", ResponseCode.FAIL);
            }

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(activationRequest.getUsername());
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

            if (!activationRequest.getOtp().equalsIgnoreCase(userAccount.getOneTimePassword())){
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
    public Response<UserAccount> getLoggedUser(UserAccount userAccount) {
        try {
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
