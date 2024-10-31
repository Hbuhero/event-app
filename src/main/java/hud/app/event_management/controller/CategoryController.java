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

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;
    private PageableConfig pageableConfig;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all-categories")
    private Page<CategoryResponseDto> getAllCategories(@RequestBody PageableParam pageableParam){
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

    @GetMapping("/events/{uuid}")
    private Page<EventResponseDto> getCategoryEvents(@PathVariable("uuid") String uuid, @RequestBody PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return categoryService.getEventsByCategoryUuid(uuid, pageable);
    }
}
