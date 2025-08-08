package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.*;
import hud.app.event_management.dto.response.LoginResponseDto;
import hud.app.event_management.exceptions.ResourceAlreadyExistException;
import hud.app.event_management.exceptions.ResourceNotFoundException;
import hud.app.event_management.model.EmailType;
import hud.app.event_management.security.jwt.JwtService;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.repository.UserAccountRepository;
import hud.app.event_management.service.AuthService;
import hud.app.event_management.userDetailService.UserDetailsImpl;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.responseUtils.ResponseCode;
import jakarta.mail.MessagingException;
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
    private final EmailService emailService;



    @Autowired
    public AuthServiceImpl(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, EmailService emailService){
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;

        this.emailService = emailService;
    }
    @Override
    public Response<String> register(UserAccountRegistrationRequest userAccountDto) throws MessagingException {

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsernameOrPhone(userAccountDto.getEmail(), userAccountDto.getPhone());

            if(optionalUserAccount.isPresent()){
                throw new ResourceAlreadyExistException("Username already exist");
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
            String nextInt = String.valueOf(random.nextInt(100001, 999999));

            userAccount.setOneTimePassword(nextInt);

            emailService.sendEmail(userAccountDto.getEmail(), EmailType.ACCOUNT_ACTIVATION, nextInt);

            userAccount.setLastOtpSentAt(LocalDateTime.now());

            userAccountRepository.save(userAccount);

            return new Response<>(false, "Registration successful: ", ResponseCode.SUCCESS, String.valueOf(nextInt));


    }

    @Override
    public Response<LoginResponseDto> login(LoginRequest loginRequest) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(loginRequest.getUsername());

            if (optionalUserAccount.isEmpty()){
                return new Response<>(true, "Login failed", ResponseCode.FAIL);
            }

            UserAccount userAccount = optionalUserAccount.get();
            String jwt = jwtService.generateToken(UserDetailsImpl.build(userAccount));

            return new Response<>(false, "Login successful", ResponseCode.SUCCESS, new LoginResponseDto(jwt, userAccount.getUsername(), userAccount.getUserType()));

    }

    @Override
    public Response<String> validateOTP(OTPVerificationRequest otpVerificationRequest) {
            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(otpVerificationRequest.getUsername());
            if (optionalUserAccount.isEmpty()){
                throw new ResourceNotFoundException("User not found");
            }

            UserAccount userAccount = optionalUserAccount.get();

            if (userAccount.isOtpVerified()){
                return new Response<>(true, "Code is already verified", ResponseCode.FAIL);
            }
            // is the otp expired
            Duration difference = Duration.between(userAccount.getLastOtpSentAt(), LocalDateTime.now());
            if (difference.getSeconds() >= duration){
                return new Response<>(true, "OTP has expired, request new OTP", ResponseCode.FAIL);
            }
            // is the otp valid
            if (!otpVerificationRequest.getOtp().equals(userAccount.getOneTimePassword())){
                return new Response<>(true, "Invalid or wrong OTP", ResponseCode.FAIL);
            }

            userAccount.setOtpVerified(true);
            userAccountRepository.save(userAccount);
            return new Response<>(false, "OTP validation successful", ResponseCode.SUCCESS);

    }

    @Override
    public Response<String> forgetPassword(String username) throws MessagingException {

            // find user
            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(username);
            if (optionalUserAccount.isEmpty()){
                throw new ResourceNotFoundException("User not found");
            }

            UserAccount userAccount = optionalUserAccount.get();
            // create otp
            Random random = new SecureRandom();
            String nextInt = String.valueOf(random.nextInt(100001, 999999));

            userAccount.setOneTimePassword(String.valueOf(nextInt));
            userAccount.setOtpVerified(false);

        emailService.sendEmail(username, EmailType.PASSWORD_RESET, nextInt);


        userAccount.setLastOtpSentAt(LocalDateTime.now());

            userAccountRepository.save(userAccount);

            return new Response<>(false, "Forget password initiated successfully: ", ResponseCode.SUCCESS, String.valueOf(nextInt));

    }

    @Override
    public Response<String> resetPassword(PasswordResetRequest passwordResetRequestDto) {
        try {


            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(passwordResetRequestDto.getUsername());

            if (optionalUserAccount.isEmpty()){
                throw new ResourceNotFoundException("User not found");
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

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(activationRequest.getUsername());
            if (optionalUserAccount.isEmpty()){
                throw new ResourceNotFoundException("User not found");
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
    public Response<String> resendOTP(String username, String action) {
        try{
            if (username.isBlank()){
                return new Response<>(true, "Username cant be null", ResponseCode.FAIL);
            }

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUsername(username);
            if (optionalUserAccount.isEmpty()){
                throw new ResourceNotFoundException("User not found");
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
            String nextInt = String.valueOf(random.nextInt(100001, 999999));

            userAccount.setOneTimePassword(nextInt);

            emailService.sendEmail(username, EmailType.valueOf(action), nextInt);


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
            return new Response<>(false, ResponseCode.SUCCESS, userAccount);
        } catch (Exception e) {
            return new Response<>(true, "Operation unsuccessful", ResponseCode.FAIL);
        }

    }


}

