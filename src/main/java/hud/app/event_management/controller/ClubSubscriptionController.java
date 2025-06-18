package hud.app.event_management.controller;

import hud.app.event_management.annotations.loggedUser.LoggedUser;
import hud.app.event_management.dto.response.ClubResponseDto;
import hud.app.event_management.dto.response.UserAccountResponseDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.service.ClubSubscriptionService;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/club-subscription")
public class ClubSubscriptionController {
    private final ClubSubscriptionService subscriptionService;
    private final PageableConfig pageableConfig;

    @Autowired
    public ClubSubscriptionController(ClubSubscriptionService subscriptionService, PageableConfig pageableConfig) {
        this.subscriptionService = subscriptionService;
        this.pageableConfig = pageableConfig;
    }

    @PostMapping("/subscribe/{uuid}")
    private Response<String> subscribe(@LoggedUser UserAccount userAccount, @PathVariable("uuid") String uuid){
        return subscriptionService.subscribe(userAccount, uuid);
    }

    @PostMapping("/unsubscribe/{uuid}")
    private Response<String> unsubscribe(@LoggedUser UserAccount userAccount, @PathVariable("uuid") String uuid){
        return subscriptionService.unsubscribe(userAccount, uuid);
    }

    @GetMapping("/{uuid}/subscribers")
    private Response<UserAccountResponseDto> getAllClubSubscribers(@PathVariable("uuid") String uuid, @RequestBody PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return subscriptionService.getAllClubSubscribers(uuid, pageable);
    }

    @GetMapping("/user/clubs")
    private Response<ClubResponseDto> getUserSubscribedClubs(@LoggedUser UserAccount userAccount, @RequestBody PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return subscriptionService.getUserSubscribedClubs(userAccount, pageable);
    }

}
