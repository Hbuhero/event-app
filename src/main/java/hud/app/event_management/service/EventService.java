package hud.app.event_management.service;

import hud.app.event_management.dto.request.EventRequest;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.utils.responseUtils.Response;
import org.springframework.data.domain.Pageable;

public interface EventService {
    Response<EventResponseDto> getEventByName(String name);

    Response<EventResponseDto> createUpdateEvent(UserAccount userAccount, EventRequest eventDto);

    Response<String> deleteEventByUuid(String uuid);

    Response<?> getAllEvents(Pageable pageable);

    Response<EventResponseDto> getEventByUuid(String uuid);

    Response<?> getEventsByCategoryUuid(String uuid, Pageable pageable);

    Response<?> getRandomEvents(Pageable pageable);
}
