package hud.app.event_management.controller;

import hud.app.event_management.dto.request.*;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.service.AuthService;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.responseUtils.ResponseCode;
import jakarta.validation.Valid;
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
    private Response<String> register(@Valid @RequestBody UserAccountRegistrationRequest userAccountDto){
        return authService.register(userAccountDto);
    }

    @PostMapping("login")
    @PreAuthorize("permitAll()")
    private Response<?> login(@Valid @RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("validate-otp")
    @PreAuthorize("permitAll()")
    private Response<String> validateOTP(@Valid @RequestBody OTPVerificationRequest otpVerificationRequest){
        return authService.validateOTP(otpVerificationRequest);
    }

    @PostMapping("activate-account")
    @PreAuthorize("permitAll()")
    private Response<UserAccount> activateAccount(@Valid @RequestBody ActivationRequest activationRequest){
        return authService.activateAccount(activationRequest);
    }

    @PostMapping("forget-password")
    @PreAuthorize("permitAll()")
    private Response<String> forgetPassword(@RequestParam String username){
        return authService.forgetPassword(username);
    }

    @PostMapping("reset-password")
    @PreAuthorize("permitAll()")
    private Response<String> resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequestDto){
        try {
            return authService.resetPassword(passwordResetRequestDto);
        } catch (Exception e) {
            return new Response<>(true, "Failed to reset password with cause: "+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @PostMapping("resend-otp")
    @PreAuthorize("permitAll()")
    private Response<String> resendOTP(@RequestParam String username){
        return authService.resendOTP(username);
    }

    // TODO: disable user account api for super admin
}
