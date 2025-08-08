package hud.app.event_management.dto.request;

import hud.app.event_management.annotations.ValidEmail;
import hud.app.event_management.annotations.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserAccountUpdateRequest {
    @NotBlank
    private String firstname;
    @NotBlank
    private String middleName;
    @NotBlank
    private String lastname;
    @ValidEmail
    private String email;
    @ValidPhoneNumber
    private String phone;
    @NotBlank
    private String address;

}
