package hud.app.event_management.service;

import hud.app.event_management.dto.request.UserEventRequestDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.model.Event;
import hud.app.event_management.model.UserEvent;
import hud.app.event_management.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserEventService {
    Response<UserEvent> createUpdateUserEvent(UserEventRequestDto userEventRequestDto);

    Response<String> deleteByUuid(String uuid);

    Response<EventResponseDto> getUpcomingEvents(Pageable pageable);

    Response<EventResponseDto> getPastEvents(Pageable pageable);
}
