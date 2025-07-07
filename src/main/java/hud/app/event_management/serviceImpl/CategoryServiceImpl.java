package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.CategoryRequest;
import hud.app.event_management.dto.response.CategoryResponseDto;
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
        try {
            Page<CategoryResponseDto> responseDto = categoryRepository.findAll(pageable).map(categoryMapper::toDto);
            return new Response<>(false, ResponseCode.SUCCESS, responseDto);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get all categories with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }

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
            System.out.println(category.getCategorySvg());
            CategoryResponseDto responseDto = categoryMapper.toDto(category);

            return new Response<>(false, ResponseCode.SUCCESS, responseDto);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get category by uuid with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
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
        try {
            if (uuid == null){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Category> optionalCategory = categoryRepository.findFirstByUuid(uuid);
            if (optionalCategory.isEmpty()){
                return new Response<>(true, "No category found", ResponseCode.NO_RECORD_FOUND);
            }

            Optional<FileUpload> optionalFileUpload = fileUtil.findByPath(optionalCategory.get().getCategoryImage());

            optionalFileUpload.ifPresent(fileUtil::setFileStatusIsDeleted);

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
    public Response<?> getUserSubscribedCategories(UserAccount userAccount, Pageable pageable) {
        try {

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
    public Response<String> addUserPreference(UserAccount userAccount, String uuid) {
        try {

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

            Optional<UserSubscribedCategory> subscribedCategory = userSubscribedCategoryRepository.findByUserAccountAndCategory(userAccount, category);
            if (subscribedCategory.isPresent()){
                return new Response<>(true, "Category is already subscribed", ResponseCode.DUPLICATE);
            }

            userSubscribedCategoryRepository.save(
                    UserSubscribedCategory.builder()
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
    public Response<String> removeUserPreference(UserAccount userAccount, String uuid) {
        try {

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

            Optional<UserSubscribedCategory> subscribedCategory = userSubscribedCategoryRepository.findByUserAccountAndCategory(userAccount, category);
            if (subscribedCategory.isEmpty()){
                return new Response<>(true, "No subscription for the provided category is found", ResponseCode.NO_RECORD_FOUND);
            }
            subscribedCategory.ifPresent(userSubscribedCategoryRepository::delete);
            return new Response<>(false, "Removed category preference successfully", ResponseCode.SUCCESS);

        } catch (Exception e) {
            return new Response<>(true, "Failed to remove user's category preference with cause:\n" + e.getMessage(), ResponseCode.FAIL);
        }
    }


    public Optional<Category> findCategory(String uuid) {
        return categoryRepository.findFirstByUuid(uuid);
    }


}
