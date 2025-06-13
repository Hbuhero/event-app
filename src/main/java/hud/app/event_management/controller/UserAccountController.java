package hud.app.event_management.controller;

import hud.app.event_management.annotations.CurrentUser;
import hud.app.event_management.dto.request.UserAccountRequest;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.service.AuthService;
import hud.app.event_management.service.UserAccountService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import hud.app.event_management.utils.userExtractor.LoggedUser;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    private Page<UserAccount> getAllUsers(@RequestBody PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return userAccountService.getAllUsers(pageable);
    }

    // get user
    @GetMapping("/{uuid}")
    @PreAuthorize("hasRole(SUPER_ADMIN')")
    private Response<UserAccount> getUserByUuid(@PathVariable("uuid") String uuid){
        return userAccountService.getUserByUuid(uuid);
    }

    // edit profile
    @PostMapping("/create-update")
    @PreAuthorize("hasRole('USER')")
    private Response<UserAccount> createUpdateUserAccount(@RequestBody UserAccountRequest userAccountDto){
        return userAccountService.createUpdateUserAccount(userAccountDto);
    }

    // delete user
    @PostMapping("/delete/{uuid}")
    @PreAuthorize("isAuthenticated()")
    private Response<String> deleteUserByUuid(@PathVariable("uuid") String uuid){
        return userAccountService.deleteUserByUuid(uuid);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    private Response<UserAccount> getUseProfile(){
        return authService.getLoggedUser();
    }

    @GetMapping("/try")
    @PreAuthorize("isAuthenticated")
    private UserAccount tryi(@CurrentUser UserAccount user){
        return user;
    }

}
