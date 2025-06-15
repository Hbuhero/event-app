package hud.app.event_management.dto.request;

import hud.app.event_management.annotations.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ActivationRequest {

    @ValidEmail
    private String username;
    @NotBlank
    private String otp;
}
