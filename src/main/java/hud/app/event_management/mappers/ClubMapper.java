package hud.app.event_management.mappers;

import hud.app.event_management.dto.response.ClubResponseDto;
import hud.app.event_management.model.Club;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClubMapper {
    ClubResponseDto toDto(Club club);
}
