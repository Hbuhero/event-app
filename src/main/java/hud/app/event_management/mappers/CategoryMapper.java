package hud.app.event_management.mappers;

import hud.app.event_management.dto.response.CategoryResponseDto;
import hud.app.event_management.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);
}
