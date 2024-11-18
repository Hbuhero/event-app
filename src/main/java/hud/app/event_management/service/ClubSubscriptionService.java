package hud.app.event_management.service;

import hud.app.event_management.dto.response.ClubResponseDto;
import hud.app.event_management.dto.response.UserAccountResponseDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.utils.Response;
import org.springframework.data.domain.Pageable;

public interface ClubSubscriptionService {
    Response<String> subscribe(String uuid);

    Response<String> unsubscribe(String uuid);

    Response<UserAccountResponseDto> getAllClubSubscribers(String uuid, Pageable pageable);

    Response<ClubResponseDto> getUserSubscribedClubs(Pageable pageable);
}
