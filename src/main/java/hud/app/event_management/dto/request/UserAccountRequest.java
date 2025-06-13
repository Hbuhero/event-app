package hud.app.event_management.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAccountRequest {
    private String firstname;
    private String middleName;
    private String lastname;
    private String email;
    private String phone;
    private String address;
    private String password;
    private String profilePhoto;
}
