package hud.app.event_management.mappers;

import hud.app.event_management.dto.response.EventTypeResponseDto;
import hud.app.event_management.model.EventType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventTypeMapper {
    EventTypeResponseDto toDto(EventType eventType);
}
