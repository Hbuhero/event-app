package hud.app.event_management.service;

import hud.app.event_management.dto.request.*;
import hud.app.event_management.dto.response.LoginResponseDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.utils.Response;

public interface AuthService {
    Response<String> register(UserAccountRequest userAccountDto);

    Response<LoginResponseDto> login(LoginRequest loginRequest);

    Response<String> validateOTP(OTPRequest otpRequest);

    Response<String> forgetPassword(String username);

    Response<String> resetPassword(PasswordRequest passwordRequestDto);

    Response<UserAccount> activateAccount(ActivationRequest activationRequest);

    Response<String> resendOTP(String username);

    Response<UserAccount> getLoggedUser();
}
