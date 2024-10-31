package hud.app.event_management.service;

import hud.app.event_management.dto.request.*;
import hud.app.event_management.dto.response.LoginResponseDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.utils.Response;

public interface AuthService {
    Response<String> register(UserAccountDto userAccountDto);

    Response<LoginResponseDto> login(LoginRequestDto loginRequestDto);

    Response<String> validateOTP(OTPRequestDto otpRequestDto);

    Response<String> forgetPassword(String username);

    Response<String> resetPassword(PasswordRequestDto passwordRequestDto);

    Response<UserAccount> activateAccount(ActivationRequestDTO activationRequestDTO);

    Response<String> resendOTP(String username);

    Response<UserAccount> getLoggedUser();
}
