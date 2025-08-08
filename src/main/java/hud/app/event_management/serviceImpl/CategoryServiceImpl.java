package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.CategoryRequest;
import hud.app.event_management.dto.response.CategoryResponseDto;
import hud.app.event_management.exceptions.ResourceAlreadyExistException;
import hud.app.event_management.exceptions.ResourceNotFoundException;
import hud.app.event_management.mappers.CategoryMapper;
import hud.app.event_management.model.*;
import hud.app.event_management.repository.CategoryRepository;
import hud.app.event_management.repository.UserSubscribedCategoryRepository;
import hud.app.event_management.service.CategoryService;
import hud.app.event_management.utils.FileUtil;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.responseUtils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final FileUtil fileUtil;
    private final UserSubscribedCategoryRepository userSubscribedCategoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper, FileUtil fileUtil, UserSubscribedCategoryRepository userSubscribedCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.fileUtil = fileUtil;
        this.userSubscribedCategoryRepository = userSubscribedCategoryRepository;
    }

    @Override
    public Response<?> getAllCategories(Pageable pageable) {

            Page<CategoryResponseDto> responseDto = categoryRepository.findAll(pageable).map(categoryMapper::toDto);
            return new Response<>(false, ResponseCode.SUCCESS, responseDto);
    }

    @Override
    public Response<CategoryResponseDto> getCategoryByUuid(String uuid) {

            if (uuid.isBlank()){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Category category = categoryRepository.findFirstByUuid(uuid).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            CategoryResponseDto responseDto = categoryMapper.toDto(category);

            return new Response<>(false, ResponseCode.SUCCESS, responseDto);

    }

    @Override
    public Response<CategoryResponseDto> createUpdateCategory(CategoryRequest categoryRequest) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findFirstByName(categoryRequest.getName());

            Category category;
            if (optionalCategory.isEmpty()){
                category = Category
                        .builder()
                        .name(categoryRequest.getName())
                        .categorySvg(categoryRequest.getCategorySvg())
                        .categoryImage(categoryRequest.getCategoryImage())
                        .description(categoryRequest.getDescription())
                        .build();
            } else {
                category = optionalCategory.get();

                Optional<FileUpload> optionalFileUpload = fileUtil.findByPath(optionalCategory.get().getCategoryImage());

                optionalFileUpload.ifPresent(fileUtil::setFileStatusIsDeleted);

               category.setCategoryImage(category.getCategoryImage());
               category.setCategorySvg(categoryRequest.getCategorySvg());
               category.setDescription(categoryRequest.getDescription());
               category.setName(categoryRequest.getName());

            }
            categoryRepository.save(category);
            return new Response<>(false, 7000, categoryMapper.toDto(category));
        } catch (Exception e) {
            return new Response<>(true, "Failed to create/update category by uuid with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }

    }

    @Override
    public Response<String> deleteCategoryByUuid(String uuid) {

            if (uuid == null){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Category category = categoryRepository.findFirstByUuid(uuid).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            Optional<FileUpload> optionalFileUpload = fileUtil.findByPath(category.getCategoryImage());

            optionalFileUpload.ifPresent(fileUtil::setFileStatusIsDeleted);

            categoryRepository.delete(category);
            return new Response<>(false, ResponseCode.SUCCESS, "Category deleted successfully");

    }

    @Override
    public Response<CategoryResponseDto> getCategoryByName(String name) {

            if (name.isBlank()){
                return new Response<>(true, "Argument should not be blank", ResponseCode.NULL_ARGUMENT);
            }

            Category category = categoryRepository.findFirstByName(name).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            CategoryResponseDto responseDto = categoryMapper.toDto(category);

            return new Response<>(false, ResponseCode.SUCCESS, responseDto);

    }

    @Override
    public Response<?> getUserSubscribedCategories(UserAccount userAccount, Pageable pageable) {

            Page<CategoryResponseDto> categoryPageable = userSubscribedCategoryRepository.findAllCategoryByUserAccount(userAccount, pageable).map(e -> categoryMapper.toDto(e.getCategory()));

            return new Response<>(false, ResponseCode.SUCCESS, categoryPageable);

    }

    @Override
    public Response<String> addUserPreference(UserAccount userAccount, String uuid) {
            if (uuid.isBlank()){
                return new Response<>(true, "Argument should not be blank", ResponseCode.NULL_ARGUMENT);
            }

           Category category = categoryRepository.findFirstByUuid(uuid).orElseThrow(() -> new ResourceNotFoundException("Category not found"));

            Optional<UserSubscribedCategory> subscribedCategory = userSubscribedCategoryRepository.findByUserAccountAndCategory(userAccount, category);
            if (subscribedCategory.isPresent()){
                throw new ResourceAlreadyExistException("Category is already subscribed");

            }

            userSubscribedCategoryRepository.save(
                    UserSubscribedCategory.builder()
                            .category(category)
                            .userAccount(userAccount)
                            .build()
            );

            return new Response<>(false, "Added new category preference successful", ResponseCode.SUCCESS);

    }

    @Override
    public Response<String> removeUserPreference(UserAccount userAccount, String uuid) {

            if (uuid.isBlank()){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Category category = categoryRepository.findFirstByUuid(uuid).orElseThrow(() -> new ResourceNotFoundException("Category not found"));


            UserSubscribedCategory subscribedCategory = userSubscribedCategoryRepository.findByUserAccountAndCategory(userAccount, category).orElseThrow(() -> new ResourceNotFoundException("No subscription for the provided category is found"));

            userSubscribedCategoryRepository.delete(subscribedCategory);
            return new Response<>(false, "Removed category preference successfully", ResponseCode.SUCCESS);

    }


}
