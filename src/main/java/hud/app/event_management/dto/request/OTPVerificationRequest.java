package hud.app.event_management.dto.request;

import hud.app.event_management.annotations.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OTPVerificationRequest {
    @ValidEmail
    private String username;
    @NotBlank
    private String otp;
}
