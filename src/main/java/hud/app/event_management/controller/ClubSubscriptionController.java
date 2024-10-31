package hud.app.event_management.controller;

import hud.app.event_management.dto.response.ClubResponseDto;
import hud.app.event_management.service.ClubSubscriptionService;
import hud.app.event_management.utils.Response;
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
    private Response<String> subscribe(@PathVariable("uuid") String uuid){
        return subscriptionService.subscribe(uuid);
    }

    @PostMapping("/unsubscribe/{uuid}")
    private Response<String> unsubscribe(@PathVariable("uuid") String uuid){
        return subscriptionService.unsubscribe(uuid);
    }

    @GetMapping("/{uuid}/subscribers")
    private Response<?> getAllClubSubscribers(@PathVariable("uuid") String uuid, @RequestBody PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return subscriptionService.getAllClubSubscribers(uuid, pageable);
    }

    @GetMapping("/user/clubs")
    private Response<ClubResponseDto> getUserSubscribedClubs(@RequestBody PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return subscriptionService.getUserSubscribedClubs(pageable);
    }

}
