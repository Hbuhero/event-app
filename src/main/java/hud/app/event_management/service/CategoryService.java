package hud.app.event_management.service;

import hud.app.event_management.dto.request.CategoryRequest;
import hud.app.event_management.dto.response.CategoryResponseDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.utils.Response;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Response<?> getAllCategories(Pageable pageable);

    Response<CategoryResponseDto> getCategoryByUuid(String uuid);

    Response<CategoryResponseDto> createUpdateCategory(CategoryRequest categoryRequest);

    Response<String> deleteCategoryByUuid(String uuid);


    Response<CategoryResponseDto> getCategoryByName(String name);

    Response<?> getUserSubscribedCategories(UserAccount userAccount, Pageable pageable);

    Response<String> addUserPreference(UserAccount userAccount, String uuid);

    Response<String> removeUserPreference(UserAccount userAccount, String uuid);
}
