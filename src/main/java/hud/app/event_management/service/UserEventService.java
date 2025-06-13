package hud.app.event_management.service;

import hud.app.event_management.dto.request.UserEventRequest;
import hud.app.event_management.model.UserEvent;
import hud.app.event_management.utils.Response;
import org.springframework.data.domain.Pageable;

public interface UserEventService {
    Response<UserEvent> createUpdateUserEvent(UserEventRequest userEventRequest);

    Response<String> deleteByUuid(String uuid);

    Response<?> getAllUserEvents(Pageable pageable);
}
