package hud.app.event_management.service;

import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.dto.response.EventTypeResponseDto;
import hud.app.event_management.model.Event;
import hud.app.event_management.model.EventType;
import hud.app.event_management.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventTypeService {
    Page<EventTypeResponseDto> getAllEventTypes(Pageable pageable);

    Response<EventTypeResponseDto> getByUuid(String uuid);

    Page<EventResponseDto> getEventsByType(String uuid, Pageable pageable);

    Response<String> deleteByUuid(String uuid);

    Response<EventTypeResponseDto> createUpdateEventType(String eventType);
}
