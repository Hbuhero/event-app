package hud.app.event_management.dto.response;

import hud.app.event_management.model.UserAccount;
import lombok.Builder;

@Builder
public class ClubResponseDto {
    private String uuid;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String picUrl;
    private UserAccount clubAdmin;
}
