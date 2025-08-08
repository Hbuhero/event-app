package hud.app.event_management.service;

import hud.app.event_management.dto.request.*;
import hud.app.event_management.dto.response.LoginResponseDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.utils.responseUtils.Response;
import jakarta.mail.MessagingException;

public interface AuthService {
    Response<String> register(UserAccountRegistrationRequest userAccountDto) throws MessagingException;

    Response<LoginResponseDto> login(LoginRequest loginRequest);

    Response<String> validateOTP(OTPVerificationRequest otpVerificationRequest);

    Response<String> forgetPassword(String username) throws MessagingException;

    Response<String> resetPassword(PasswordResetRequest passwordResetRequestDto);

    Response<UserAccount> activateAccount(ActivationRequest activationRequest);

    Response<String> resendOTP(String username, String action);

    Response<UserAccount> getLoggedUser(UserAccount userAccount);
}
