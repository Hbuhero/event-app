package hud.app.event_management.dto.request;

import hud.app.event_management.annotations.ValidEmail;
import hud.app.event_management.annotations.ValidPhoneNumber;
import hud.app.event_management.annotations.loggedUser.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@ValidPassword
public class UserAccountRegistrationRequest {
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
    private String address;
    private String password;
    private String confirmPassword;
    // TODO: file service
    private String profilePhoto;
}
