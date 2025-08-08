package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.UserAccountUpdateRequest;
import hud.app.event_management.model.*;
import hud.app.event_management.repository.UserAccountRepository;
import hud.app.event_management.repository.UserEventRepository;
import hud.app.event_management.repository.UserSubscribedCategoryRepository;
import hud.app.event_management.service.UserAccountService;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.responseUtils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserSubscribedCategoryRepository userSubscribedCategoryRepository;
    private final UserEventRepository userEventRepository;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, UserSubscribedCategoryRepository userSubscribedCategoryRepository, UserEventRepository userEventRepository){
        this.userAccountRepository = userAccountRepository;
        this.userSubscribedCategoryRepository = userSubscribedCategoryRepository;
        this.userEventRepository = userEventRepository;
    }

    @Override
    public Response<String> deleteUserByUuid( UserAccount userAccount, String uuid) {
        try {

            if (!userAccount.getUuid().equals(uuid)){
                return new Response<>(true, "Invalid operation", ResponseCode.FAIL);
            }

            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUuid(uuid);
            if (optionalUserAccount.isEmpty()){
                return new Response<>(true, "User not found", ResponseCode.USER_NOT_FOUND);
            }

            userAccountRepository.delete(optionalUserAccount.get());
            return new Response<>(false, ResponseCode.SUCCESS, "User deleted successfully");
        } catch (Exception e) {
            return new Response<>(true, "Failed to delete user with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<String> changeUserNotificationSetting(UserAccount userAccount){
        if (userAccount == null) {
            return new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
        }

        userAccount.setNotify(!userAccount.getNotify());

        List<UserEvent> userEvents = userEventRepository.findAllByUserAccount(userAccount);
        userEvents.forEach(userEvent -> {
            userEvent.setNotify(!userEvent.getNotify());
        });

        List<UserSubscribedCategory> userSubscribedCategories = userSubscribedCategoryRepository.findAllByUserAccount(userAccount);
        userSubscribedCategories.forEach(userSubscribedCategory -> {
            userSubscribedCategory.setNotify(!userSubscribedCategory.getNotify());
        });
         userEventRepository.saveAll(userEvents);
        userSubscribedCategoryRepository.saveAll(userSubscribedCategories);
        userAccountRepository.save(userAccount);

        return new Response<>(true, ResponseCode.SUCCESS, "User notification setting updated successfully");

    }

    @Override
    public Response<UserAccount> updateUserAccount(UserAccount userAccount, UserAccountUpdateRequest userAccountDto) {
        try {

            if (userAccount == null) {
                return new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
            }

            userAccount.setFirstName(userAccountDto.getFirstname());
            userAccount.setMiddleName(userAccountDto.getMiddleName());
            userAccount.setLastName(userAccountDto.getLastname());
            userAccount.setAddress(userAccountDto.getAddress());
            userAccount.setPhone(userAccountDto.getPhone());

            UserAccount response = userAccountRepository.save(userAccount);

            return new Response<>(false, "Successfully updated profile", ResponseCode.SUCCESS, response);
        } catch (Exception e) {
            return new Response<>(true, "Failed to update user account with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<UserAccount> getUserByUuid(String uuid) {
        try {
            Optional<UserAccount> optionalUserAccount = userAccountRepository.findFirstByUuid(uuid);
            if (optionalUserAccount.isEmpty()){
                return new Response<>(true, "User not found", ResponseCode.USER_NOT_FOUND);
            }

            UserAccount userAccount = optionalUserAccount.get();
            return new Response<>(false, ResponseCode.SUCCESS, userAccount);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get user with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Page<UserAccount> getAllUsers(Pageable pageable) {
        try {
            return userAccountRepository.findAll(pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PageImpl<>(new ArrayList<>());
    }

}
