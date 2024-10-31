package hud.app.event_management.controller;

import hud.app.event_management.dto.request.*;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.service.AuthService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("register")
    private Response<String> register(@RequestBody UserAccountDto userAccountDto){
        return authService.register(userAccountDto);
    }

    @PostMapping("login")
    private Response<?> login(@RequestBody LoginRequestDto loginRequestDto){
        return authService.login(loginRequestDto);
    }

    @PostMapping("validate-otp")
    private Response<String> validateOTP(@RequestBody OTPRequestDto otpRequestDto){
        return authService.validateOTP(otpRequestDto);
    }

    @PostMapping("activate-account")
    private Response<UserAccount> activateAccount(@RequestBody ActivationRequestDTO activationRequestDTO){
        return authService.activateAccount(activationRequestDTO);
    }

    @PostMapping("forget-password")
    private Response<String> forgetPassword(@RequestParam String username){
        return authService.forgetPassword(username);
    }

    @PostMapping("reset-password")
    private Response<String> resetPassword(@RequestBody PasswordRequestDto passwordRequestDto){
        try {
            return authService.resetPassword(passwordRequestDto);
        } catch (Exception e) {
            return new Response<>(true, "Failed to reset password with cause: "+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @PostMapping("resend-otp")
    private Response<String> resendOTP(@RequestParam String username){
        return authService.resendOTP(username);
    }
}
