package hud.app.event_management.controller;

import hud.app.event_management.dto.request.UserAccountDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.service.AuthService;
import hud.app.event_management.service.UserAccountService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private Page<UserAccount> getAllUsers(@RequestBody PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return userAccountService.getAllUsers(pageable);
    }

    // get user
    @GetMapping("/{uuid}")
    private Response<UserAccount> getUserByUuid(@PathVariable("uuid") String uuid){
        return userAccountService.getUserByUuid(uuid);
    }

    // edit profile
    @PostMapping("/create-update")
    private Response<UserAccount> createUpdateUserAccount(@RequestBody UserAccountDto userAccountDto){
        return userAccountService.createUpdateUserAccount(userAccountDto);
    }

    // delete user
    @PostMapping("/delete/{uuid}")
    private Response<String> deleteUserByUuid(@PathVariable("uuid") String uuid){
        return userAccountService.deleteUserByUuid(uuid);
    }

    @GetMapping("/profile")
    private Response<UserAccount> getUseProfile(){
        return authService.getLoggedUser();
    }

}
