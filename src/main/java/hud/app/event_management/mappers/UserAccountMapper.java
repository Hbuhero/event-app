package hud.app.event_management.mappers;

import hud.app.event_management.dto.response.UserAccountResponseDto;
import hud.app.event_management.model.UserAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    UserAccountResponseDto toDto(UserAccount userAccount);
}
