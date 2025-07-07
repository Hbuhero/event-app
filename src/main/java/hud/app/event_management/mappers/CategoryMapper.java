package hud.app.event_management.mappers;

import hud.app.event_management.dto.response.CategoryResponseDto;
import hud.app.event_management.model.Category;
import hud.app.event_management.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "events", target = "eventCount")
    @Mapping(source = "categorySvg", target = "svg")
    CategoryResponseDto toDto(Category category);

    default long mapEventCount(List<Event> events) {
        return events != null ? events.size() : 0;  // Return 0 if events is null
    }
}
