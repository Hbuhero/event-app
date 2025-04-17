package hud.app.event_management.controller;

import hud.app.event_management.dto.request.UserEventRequestDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.model.Event;
import hud.app.event_management.model.UserEvent;
import hud.app.event_management.service.UserEventService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private Response<UserEvent> createUpdateUserEvent(@RequestBody UserEventRequestDto userEventRequestDto){
        return userEventService.createUpdateUserEvent(userEventRequestDto);
    }

    @PostMapping("delete/{uuid}")
    private Response<String> deleteByUuid(@PathVariable("uuid") String uuid){
        return userEventService.deleteByUuid(uuid);
    }

    @GetMapping("all")
    private Response<?> getAllUserEvents( PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return userEventService.getAllUserEvents(pageable);
    }
}
