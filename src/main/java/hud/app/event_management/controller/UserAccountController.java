package hud.app.event_management.controller;

import hud.app.event_management.annotations.loggedUser.LoggedUser;
import hud.app.event_management.dto.request.PasswordResetRequest;
import hud.app.event_management.dto.request.UserAccountUpdateRequest;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.service.AuthService;
import hud.app.event_management.service.UserAccountService;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/user/")
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final AuthService authService;
    private PageableConfig pageableConfig;

    @Autowired
    public UserAccountController(UserAccountService userAccountService, AuthService authService){
        this.userAccountService = userAccountService;
        this.authService = authService;
    }

    // get all users
    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    private Page<UserAccount> getAllUsers(PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return userAccountService.getAllUsers(pageable);
    }

    // get user
    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    private Response<UserAccount> getUserByUuid(@PathVariable("uuid") String uuid){
        return userAccountService.getUserByUuid(uuid);
    }

    // edit profile
    @PostMapping("/create-update")
    @PreAuthorize("hasRole('USER')")
    private Response<UserAccount> updateUserAccount(@LoggedUser UserAccount userAccount, @Valid @RequestBody UserAccountUpdateRequest userAccountDto){
        return userAccountService.updateUserAccount(userAccount, userAccountDto);
    }

    // delete user
    @PostMapping("/delete/{uuid}")
    @PreAuthorize("isAuthenticated()")
    private Response<String> deleteUserByUuid(@LoggedUser UserAccount userAccount, @PathVariable("uuid") String uuid){
        return userAccountService.deleteUserByUuid(userAccount, uuid);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    private Response<UserAccount> getUseProfile(@LoggedUser UserAccount userAccount){
        return authService.getLoggedUser(userAccount);
    }

    @PostMapping("/change-notification-settings")
    @PreAuthorize("isAuthenticated()")
    private Response<String> changeNotificationSettings(@LoggedUser UserAccount userAccount){
        return userAccountService.changeUserNotificationSetting(userAccount);
    }


}
