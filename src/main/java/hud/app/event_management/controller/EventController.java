package hud.app.event_management.controller;

import hud.app.event_management.annotations.loggedUser.LoggedUser;
import hud.app.event_management.dto.request.EventRequest;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.service.EventService;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/event")
public class EventController {
    private final EventService eventService;
    private final PageableConfig pageableConfig;

    @Autowired
    public EventController(EventService eventService, PageableConfig pageableConfig) {
        this.eventService = eventService;
        this.pageableConfig = pageableConfig;
    }

    // get event by uuid
    @GetMapping("/{uuid}")
    @PreAuthorize("permitAll()")
    private Response<EventResponseDto> getEventByUuid(@PathVariable("uuid") String uuid){
        return eventService.getEventByUuid(uuid);
    }

    // get all events
    @GetMapping("/all")
    @PreAuthorize("permitAll()")
    private Response<?> getAllEvents(PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return eventService.getAllEvents(pageable);
    }

    // create event update event
    @PostMapping(path = "create-update")
    @PreAuthorize("isAuthenticated()")
    private Response<EventResponseDto> createUpdateEvent(@LoggedUser UserAccount userAccount, @Valid EventRequest eventDto){
        return eventService.createUpdateEvent(userAccount, eventDto);
    }

    // delete event
    @PostMapping("delete/{uuid}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    private Response<String> deleteEventByUuid(@PathVariable("uuid") String uuid){
        return eventService.deleteEventByUuid(uuid);
    }

    @GetMapping("/name")
    @PreAuthorize("permitAll()")
    private Response<?> getEventByName(@RequestParam("name") String name){
        System.out.println(name);
        return eventService.getEventByName(name);
    }

    @GetMapping("/category/{uuid}")
    @PreAuthorize("permitAll()")
    private Response<?> getCategoryEvents(@PathVariable("uuid") String uuid, PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return eventService.getEventsByCategoryUuid(uuid, pageable);
    }

    @GetMapping("/random")
    @PreAuthorize("permitAll()")
    private Response<?> getRandomEvents(PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return eventService.getRandomEvents(pageable);
    }
}
