package hud.app.event_management.controller;

import hud.app.event_management.annotations.loggedUser.LoggedUser;
import hud.app.event_management.dto.request.UserEventRequest;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.model.UserEvent;
import hud.app.event_management.service.UserEventService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user-event")
public class UserEventController {
    private final UserEventService userEventService;
    private final PageableConfig pageableConfig;

    @Autowired
    public UserEventController(UserEventService userEventService, PageableConfig pageableConfig) {
        this.userEventService = userEventService;
        this.pageableConfig = pageableConfig;
    }

    @PostMapping("create-update")
    @PreAuthorize("isAuthenticated()")
    private Response<UserEvent> createUpdateUserEvent(@LoggedUser UserAccount userAccount, @Valid @RequestBody UserEventRequest userEventRequest){
        return userEventService.createUpdateUserEvent(userAccount, userEventRequest);
    }

    @PostMapping("delete/{uuid}")
    @PreAuthorize("isAuthenticated()")
    private Response<String> deleteByUuid(@LoggedUser UserAccount userAccount, @PathVariable("uuid") String uuid){
        return userEventService.deleteByUuid(userAccount, uuid);
    }

    @GetMapping("all")
    @PreAuthorize("isAuthenticated()")
    private Response<?> getAllUserEvents(@LoggedUser UserAccount userAccount, PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return userEventService.getAllUserEvents(userAccount, pageable);
    }
}
