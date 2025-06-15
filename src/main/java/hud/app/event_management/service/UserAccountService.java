package hud.app.event_management.service;

import hud.app.event_management.dto.request.UserAccountRegistrationRequest;
import hud.app.event_management.dto.request.UserAccountUpdateRequest;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAccountService {


    Response<String> deleteUserByUuid(UserAccount userAccount, String uuid);

    Response<UserAccount> updateUserAccount(UserAccount userAccount, UserAccountUpdateRequest userAccountDto);

    Response<UserAccount> getUserByUuid(String uuid);

    Page<UserAccount> getAllUsers(Pageable pageable);
}
