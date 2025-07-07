package hud.app.event_management.dto.request;

import hud.app.event_management.annotations.ValidEmail;
import hud.app.event_management.annotations.ValidPassword;
import lombok.Data;

@Data
@ValidPassword
public class PasswordResetRequest {
    @ValidEmail
    private String username;
    private String password;
    private String confirmPassword;
}
