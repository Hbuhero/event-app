package hud.app.event_management.controller;

import hud.app.event_management.dto.request.*;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.service.AuthService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("permitAll()")
    private Response<String> register(@RequestBody UserAccountRequest userAccountDto){
        return authService.register(userAccountDto);
    }

    @PostMapping("login")
    @PreAuthorize("permitAll()")
    private Response<?> login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("validate-otp")
    @PreAuthorize("permitAll()")
    private Response<String> validateOTP(@RequestBody OTPRequest otpRequest){
        return authService.validateOTP(otpRequest);
    }

    @PostMapping("activate-account")
    @PreAuthorize("permitAll()")
    private Response<UserAccount> activateAccount(@RequestBody ActivationRequest activationRequest){
        return authService.activateAccount(activationRequest);
    }

    @PostMapping("forget-password")
    @PreAuthorize("permitAll()")
    private Response<String> forgetPassword(@RequestParam String username){
        return authService.forgetPassword(username);
    }

    @PostMapping("reset-password")
    @PreAuthorize("permitAll()")
    private Response<String> resetPassword(@RequestBody PasswordRequest passwordRequestDto){
        try {
            return authService.resetPassword(passwordRequestDto);
        } catch (Exception e) {
            return new Response<>(true, "Failed to reset password with cause: "+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @PostMapping("resend-otp")
    @PreAuthorize("permitAll()")
    private Response<String> resendOTP(@RequestParam String username){
        return authService.resendOTP(username);
    }
}
