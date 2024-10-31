package hud.app.event_management.mappers;

import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "type", source = "type.type")
    @Mapping(target = "category", source = "category.category")
    EventResponseDto eventToDto(Event event);
}
