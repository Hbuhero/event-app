package hud.app.event_management.dto.request;

import hud.app.event_management.annotations.ValidEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;


@Data
public class ActivationRequest {

    @ValidEmail
    private String username;
    @NotBlank
    private String otp;
}
