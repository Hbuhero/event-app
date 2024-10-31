package hud.app.event_management.service;

import hud.app.event_management.dto.request.UserAccountDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserAccountService {


    Response<String> deleteUserByUuid(String uuid);

    Response<UserAccount> createUpdateUserAccount(UserAccountDto userAccountDto);

    Response<UserAccount> getUserByUuid(String uuid);

    Page<UserAccount> getAllUsers(Pageable pageable);
}
