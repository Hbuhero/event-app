package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.CategoryRequestDto;
import hud.app.event_management.dto.response.CategoryResponseDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.mappers.CategoryMapper;
import hud.app.event_management.mappers.EventMapper;
import hud.app.event_management.model.Category;
import hud.app.event_management.model.Event;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.model.UserSubscribedCategories;
import hud.app.event_management.repository.CategoryRepository;
import hud.app.event_management.repository.EventRepository;
import hud.app.event_management.repository.UserSubscribedCategoryRepository;
import hud.app.event_management.service.CategoryService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.ResponseCode;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import hud.app.event_management.utils.userExtractor.LoggedUser;
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
    private final CategoryMapper categoryMapper;
    private final LoggedUser loggedUser;
    private final UserSubscribedCategoryRepository userSubscribedCategoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository, EventMapper eventMapper,
                               CategoryMapper categoryMapper, LoggedUser loggedUser, UserSubscribedCategoryRepository userSubscribedCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.loggedUser = loggedUser;
        this.userSubscribedCategoryRepository = userSubscribedCategoryRepository;
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
            Optional<Category> optionalCategory = categoryRepository.findFirstByName(categoryRequestDto.getName());

            if (optionalCategory.isEmpty()){
                Category category = categoryRepository.save(
                        Category
                                .builder()
                                .name(categoryRequestDto.getName())
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

    @Override
    public Response<CategoryResponseDto> getCategoryByName(String name) {
        try {
            if (name == null){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Category> optionalCategory = categoryRepository.findFirstByName(name);
            if (optionalCategory.isEmpty()){
                return new Response<>(true, "No category found", ResponseCode.NO_RECORD_FOUND);
            }


            CategoryResponseDto responseDto = categoryMapper.toDto(optionalCategory.get());

            return new Response<>(false, ResponseCode.SUCCESS, responseDto);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get category by name with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<?> getUserSubscribedCategories(Pageable pageable) {
        try {
            UserAccount userAccount = loggedUser.getUser();
            if (userAccount == null){
                return new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
            }

            Page<CategoryResponseDto> categoryPageable = userSubscribedCategoryRepository.findAllCategoryByUserAccount(userAccount, pageable).map(e -> categoryMapper.toDto(e.getCategory()));

            return new Response<>(false, ResponseCode.SUCCESS, categoryPageable);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get user's category preference with cause: \n" + e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<String> addUserPreference(String uuid) {
        try {
            UserAccount userAccount = loggedUser.getUser();
            if (userAccount == null){
                return new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
            }
            if (uuid == null){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Category> optionalCategory = categoryRepository.findFirstByUuid(uuid);
            if (optionalCategory.isEmpty()){
                return new Response<>(true, "No category found", ResponseCode.NO_RECORD_FOUND);
            }

            Category category = optionalCategory.get();
            userSubscribedCategoryRepository.save(
                    UserSubscribedCategories.builder()
                            .category(category)
                            .userAccount(userAccount)
                            .build()
            );

            return new Response<>(false, "Added new category preference successful", ResponseCode.SUCCESS);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get user's category preference with cause:\n" + e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<String> removeUserPreference(String uuid) {
        try {
            UserAccount userAccount = loggedUser.getUser();
            if (userAccount == null){
                return new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
            }

            if (uuid == null){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Category> optionalCategory = categoryRepository.findFirstByUuid(uuid);
            if (optionalCategory.isEmpty()){
                return new Response<>(true, "No category found", ResponseCode.NO_RECORD_FOUND);
            }

            Category category = optionalCategory.get();

            userSubscribedCategoryRepository.deleteByUserAccountAndCategory(userAccount, category);

            return new Response<>(false, "Removed category preference successfully", ResponseCode.SUCCESS);

        } catch (Exception e) {
            return new Response<>(true, "Failed to get user's category preference with cause:\n" + e.getMessage(), ResponseCode.FAIL);
        }
    }


    public Optional<Category> findCategory(String uuid) {
        return categoryRepository.findFirstByUuid(uuid);
    }


}
