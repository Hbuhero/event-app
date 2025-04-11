package hud.app.event_management.service;

import hud.app.event_management.dto.request.EventDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.model.Event;
import hud.app.event_management.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {
    Response<EventResponseDto> getEventByName(String name);

    Response<EventResponseDto> createUpdateEvent(EventDto eventDto);

    Response<String> deleteEventByUuid(String uuid);

    Page<EventResponseDto> getAllEvents(Pageable pageable);

    Response<EventResponseDto> getEventByUuid(String uuid);

    Response<?> getEventsByCategoryUuid(String uuid, Pageable pageable);

    Response<?> getRandomEvents(Pageable pageable);
}
