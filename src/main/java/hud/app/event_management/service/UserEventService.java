package hud.app.event_management.service;

import hud.app.event_management.dto.request.UserEventRequest;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.model.UserEvent;
import hud.app.event_management.utils.responseUtils.Response;
import org.springframework.data.domain.Pageable;

public interface UserEventService {
    Response<UserEvent> createUpdateUserEvent(UserAccount userAccount, UserEventRequest userEventRequest);

    Response<String> deleteByUuid(UserAccount userAccount, String uuid);

    Response<?> getAllUserEvents( UserAccount userAccount,Pageable pageable);
}
