package hud.app.event_management.controller;

import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.dto.response.EventTypeResponseDto;
import hud.app.event_management.service.EventTypeService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/event-type")
public class EventTypeController {
    private final EventTypeService eventTypeService;
    private PageableConfig pageableConfig;

    @Autowired
    public EventTypeController(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @GetMapping("/all")
    private Page<EventTypeResponseDto> getAllEventTypes(@RequestBody PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return eventTypeService.getAllEventTypes(pageable);
    }

    @GetMapping("/{uuid}")
    private Response<EventTypeResponseDto> getByUuid(@PathVariable("uuid") String uuid){
        return eventTypeService.getByUuid(uuid);
    }

    @GetMapping("/events")
    private Page<EventResponseDto> getEventsByType(@PathVariable("uuid")String uuid, @RequestBody PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return eventTypeService.getEventsByType(uuid, pageable);
    }

    @PostMapping("/delete/{uuid}")
    private Response<String> deleteByUuid(@PathVariable("uuid") String uuid){
        return eventTypeService.deleteByUuid(uuid);
    }

    @PostMapping("create-update")
    //todo: annotate the param
    private Response<EventTypeResponseDto> createUpdateEventType(String eventType){
        return eventTypeService.createUpdateEventType(eventType);
    }

}
