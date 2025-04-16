package hud.app.event_management.service;

import hud.app.event_management.dto.request.CategoryRequestDto;
import hud.app.event_management.dto.response.CategoryResponseDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.model.Category;
import hud.app.event_management.model.Event;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryResponseDto> getAllCategories(Pageable pageable);

    Response<CategoryResponseDto> getCategoryByUuid(String uuid);

    Response<CategoryResponseDto> createUpdateCategory(CategoryRequestDto categoryRequestDto);

    Response<String> deleteCategoryByUuid(String uuid);


    Response<CategoryResponseDto> getCategoryByName(String name);

    Response<?> getUserSubscribedCategories(Pageable pageable);

    Response<String> addUserPreference(String uuid);

    Response<String> removeUserPreference(String uuid);
}
