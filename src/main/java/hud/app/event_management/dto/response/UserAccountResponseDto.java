package hud.app.event_management.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserAccountResponseDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String username;
    private String phone;
    private String userType;
    private String address;
    private String profilePhoto;
}
