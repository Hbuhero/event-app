package hud.app.event_management.service;

import hud.app.event_management.dto.response.ClubResponseDto;
import hud.app.event_management.dto.response.UserAccountResponseDto;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.utils.responseUtils.Response;
import org.springframework.data.domain.Pageable;

public interface ClubSubscriptionService {
    Response<String> subscribe(UserAccount userAccount, String uuid);

    Response<String> unsubscribe(UserAccount userAccount, String uuid);

    Response<UserAccountResponseDto> getAllClubSubscribers(String uuid, Pageable pageable);

    Response<ClubResponseDto> getUserSubscribedClubs(UserAccount userAccount, Pageable pageable);
}
