package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.CategoryRequestDto;
import hud.app.event_management.dto.response.CategoryResponseDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.mappers.CategoryMapper;
import hud.app.event_management.mappers.EventMapper;
import hud.app.event_management.model.Category;
import hud.app.event_management.model.Event;
import hud.app.event_management.repository.CategoryRepository;
import hud.app.event_management.repository.EventRepository;
import hud.app.event_management.service.CategoryService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository, EventMapper eventMapper,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Page<CategoryResponseDto> getAllCategories(Pageable pageable) {
        try {
            return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageImpl<>(new ArrayList<>());
    }

    @Override
    public Response<CategoryResponseDto> getCategoryByUuid(String uuid) {
        try {
            if (uuid == null){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Category> optionalCategory = categoryRepository.findFirstByUuid(uuid);
            if (optionalCategory.isEmpty()){
                return new Response<>(true, "No category found", ResponseCode.NO_RECORD_FOUND);
            }

            Category category = optionalCategory.get();

            CategoryResponseDto responseDto = categoryMapper.toDto(category);

            return new Response<>(false, ResponseCode.SUCCESS, responseDto);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get category by uuid with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<CategoryResponseDto> createUpdateCategory(CategoryRequestDto categoryRequestDto) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findFirstByCategory(categoryRequestDto.getCategory());

            if (optionalCategory.isEmpty()){
                Category category = categoryRepository.save(
                        Category
                                .builder()
                                .category(categoryRequestDto.getCategory())
                        .build()
                );

                return new Response<>(false, 7000, categoryMapper.toDto(category));
            } else {
//                todo
                return null;
            }
        } catch (Exception e) {
            return new Response<>(true, "Failed to create/update category by uuid with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }

    }

    @Override
    public Response<String> deleteCategoryByUuid(String uuid) {
        try {
            if (uuid == null){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Category> optionalCategory = categoryRepository.findFirstByUuid(uuid);
            if (optionalCategory.isEmpty()){
                return new Response<>(true, "No category found", ResponseCode.NO_RECORD_FOUND);
            }

            categoryRepository.delete(optionalCategory.get());
            return new Response<>(false, ResponseCode.SUCCESS, "Category deleted successfully");
        } catch (Exception e) {
            return new Response<>(true, "Failed to delete category by uuid with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }




    public Optional<Category> findCategory(String uuid) {
        return categoryRepository.findFirstByUuid(uuid);
    }


}
