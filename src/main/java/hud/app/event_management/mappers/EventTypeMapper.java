package hud.app.event_management.mappers;

import hud.app.event_management.dto.response.EventTypeResponseDto;
import hud.app.event_management.model.Event;
import hud.app.event_management.model.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventTypeMapper {

    @Mapping(source = "events", target = "eventCount")
    EventTypeResponseDto toDto(EventType eventType);

    default long mapEventCount(List<Event> events) {
        return events != null ? events.size() : 0;  // Return 0 if events is null
    }
}
