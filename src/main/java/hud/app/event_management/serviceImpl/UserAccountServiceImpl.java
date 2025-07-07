package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.UserAccountUpdateRequest;
import hud.app.event_management.model.EntityType;
import hud.app.event_management.model.FileUpload;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.repository.FileRepository;
import hud.app.event_management.repository.UserAccountRepository;
import hud.app.event_management.service.UserAccountService;
import hud.app.event_management.utils.FileUtil;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.responseUtils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final FileUtil fileUtil;


    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository, FileUtil fileUtil){
        this.userAccountRepository = userAccountRepository;

        this.fileUtil = fileUtil;
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
    public Response<UserAccount> updateUserAccount(UserAccount userAccount, UserAccountUpdateRequest userAccountDto) {
        try {

            if (userAccount == null) {
                return new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
            }

            Optional<FileUpload> optionalFileUpload = fileUtil.findByPath(userAccount.getProfilePhoto());
            optionalFileUpload.ifPresent(fileUtil::setFileStatusIsDeleted);

            userAccount.setFirstName(userAccountDto.getFirstname());
            userAccount.setMiddleName(userAccountDto.getMiddleName());
            userAccount.setLastName(userAccountDto.getLastname());
            userAccount.setAddress(userAccountDto.getAddress());
            userAccount.setPhone(userAccountDto.getPhone());
            userAccount.setProfilePhoto(userAccountDto.getProfilePhoto());

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
