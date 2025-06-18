package hud.app.event_management.controller;

import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.dto.response.EventTypeResponseDto;
import hud.app.event_management.service.EventTypeService;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/event-type")
public class EventTypeController {
    private final EventTypeService eventTypeService;
    private final PageableConfig pageableConfig;

    @Autowired
    public EventTypeController(EventTypeService eventTypeService, PageableConfig pageableConfig) {
        this.eventTypeService = eventTypeService;
        this.pageableConfig = pageableConfig;
    }

    @GetMapping("/all")
    @PreAuthorize("permitAll()")
    private Page<EventTypeResponseDto> getAllEventTypes(PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return eventTypeService.getAllEventTypes(pageable);
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("permitAll()")
    private Response<EventTypeResponseDto> getByUuid(@PathVariable("uuid") String uuid){
        return eventTypeService.getByUuid(uuid);
    }

    @GetMapping("/events")
    @PreAuthorize("permitAll()")
    private Page<EventResponseDto> getEventsByType(@PathVariable("uuid")String uuid, PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return eventTypeService.getEventsByType(uuid, pageable);
    }

    @PostMapping("/delete/{uuid}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    private Response<String> deleteByUuid(@PathVariable("uuid") String uuid){
        return eventTypeService.deleteByUuid(uuid);
    }

    @PostMapping("create-update")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    private Response<EventTypeResponseDto> createUpdateEventType(@RequestParam("eventType") String eventType){
        return eventTypeService.createUpdateEventType(eventType);
    }

}
