package hud.app.event_management.controller;

import hud.app.event_management.dto.request.CategoryRequestDto;
import hud.app.event_management.dto.response.CategoryResponseDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.model.Category;
import hud.app.event_management.model.Event;
import hud.app.event_management.repository.CategoryRepository;
import hud.app.event_management.service.CategoryService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

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
    private Page<CategoryResponseDto> getAllCategories(PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return categoryService.getAllCategories(pageable);
    }

    @GetMapping("/{uuid}")
    private Response<CategoryResponseDto> getCategoryByUuid(@PathVariable("uuid") String uuid){
        return categoryService.getCategoryByUuid(uuid);
    }

    @PostMapping("/create-update")
    private Response<CategoryResponseDto> createUpdateCategory(@RequestBody CategoryRequestDto categoryRequestDto){
        return categoryService.createUpdateCategory(categoryRequestDto);
    }

    @PostMapping("/delete/{uuid}")
    private Response<String> deleteCategoryByUuid(@PathVariable("uuid") String uuid){
        return categoryService.deleteCategoryByUuid(uuid);
    }

    @GetMapping("/name/{name}")
    private Response<CategoryResponseDto> getCategoryByName(@PathVariable("name") String name){
        return categoryService.getCategoryByName(name);
    }

    @GetMapping("/user-preference")
    private Response<?> getUserSubscribedCategory(PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return categoryService.getUserSubscribedCategories(pageable);
    }

    @PostMapping("/user-preference/add/{uuid}")
    private Response<String> addUserPreference(@PathVariable("uuid") String uuid){
        return categoryService.addUserPreference(uuid);
    }

    @PostMapping("/user-preference/remove/{uuid}")
    private Response<String> removeUserPreference(@PathVariable("uuid") String uuid){
        return categoryService.removeUserPreference(uuid);
    }

}
