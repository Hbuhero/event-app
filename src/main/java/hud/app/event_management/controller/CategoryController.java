package hud.app.event_management.controller;

import hud.app.event_management.annotations.loggedUser.LoggedUser;
import hud.app.event_management.dto.request.CategoryRequest;
import hud.app.event_management.dto.response.CategoryResponseDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.service.CategoryService;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final PageableConfig pageableConfig;

    @Autowired
    public CategoryController(CategoryService categoryService, PageableConfig pageableConfig) {
        this.categoryService = categoryService;
        this.pageableConfig = pageableConfig;
    }

    @GetMapping("/all-categories")
    @PreAuthorize("permitAll()")
    private Response<?> getAllCategories(PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return categoryService.getAllCategories(pageable);
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("permitAll()")
    private Response<CategoryResponseDto> getCategoryByUuid(@PathVariable("uuid") String uuid){
        return categoryService.getCategoryByUuid(uuid);
    }

    @PostMapping("/create-update")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    private Response<CategoryResponseDto> createUpdateCategory(@Valid @ModelAttribute CategoryRequest categoryRequest){
        return categoryService.createUpdateCategory(categoryRequest);
    }

    @PostMapping("/delete/{uuid}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    private Response<String> deleteCategoryByUuid(@PathVariable("uuid") String uuid){
        return categoryService.deleteCategoryByUuid(uuid);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("permitAll()")
    private Response<CategoryResponseDto> getCategoryByName(@PathVariable("name") String name){
        return categoryService.getCategoryByName(name);
    }

    @GetMapping("/user-preference")
    @PreAuthorize("hasRole('USER')")
    private Response<?> getUserSubscribedCategory(@LoggedUser UserAccount userAccount, PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return categoryService.getUserSubscribedCategories(userAccount,pageable);
    }

    @PostMapping("/user-preference/add/{uuid}")
    @PreAuthorize("hasRole('USER')")
    private Response<String> addUserPreference(@LoggedUser UserAccount userAccount, @PathVariable("uuid") String uuid){
        return categoryService.addUserPreference(userAccount,uuid);
    }

    @PostMapping("/user-preference/delete/{uuid}")
    @PreAuthorize("hasRole('USER')")
    private Response<String> removeUserPreference(@LoggedUser UserAccount userAccount, @PathVariable("uuid") String uuid){
        return categoryService.removeUserPreference(userAccount,uuid);
    }

}
