package hud.app.event_management.controller;

import hud.app.event_management.dto.request.EventRequest;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.model.Event;
import hud.app.event_management.service.EventService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("create-update")
    @PreAuthorize("isAuthenticated()")
    private Response<EventResponseDto> createUpdateEvent(@RequestBody EventRequest eventDto){
        return eventService.createUpdateEvent(eventDto);
    }

    // delete event
    @PostMapping("delete/{uuid}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    private Response<String> deleteEventByUuid(@PathVariable("uuid") String uuid){
        return eventService.deleteEventByUuid(uuid);
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    private Response<EventResponseDto> getEventByName(@RequestParam("name") String name){
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
