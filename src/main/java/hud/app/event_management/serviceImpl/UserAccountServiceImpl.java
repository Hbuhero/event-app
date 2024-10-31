package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.UserAccountDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.repository.UserAccountRepository;
import hud.app.event_management.service.UserAccountService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.ResponseCode;
import hud.app.event_management.utils.userExtractor.LoggedUser;
import org.apache.catalina.User;
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
    private LoggedUser loggedUser;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository){
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public Response<String> deleteUserByUuid(String uuid) {
        try {
            UserAccount userAccount = loggedUser.getUser();
            if (userAccount == null){
                return new Response<>(true, "Full authorization is required", ResponseCode.UNAUTHORIZED);
            }

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
    public Response<UserAccount> createUpdateUserAccount(UserAccountDto userAccountDto) {
        // todo: update api
        return null;
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
