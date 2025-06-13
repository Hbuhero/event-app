package hud.app.event_management.service;

import hud.app.event_management.dto.request.ClubRequest;
import hud.app.event_management.dto.response.ClubResponseDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClubService {
    Response<ClubResponseDto> createUpdateClub(ClubRequest clubRequest);

    Response<String> deleteByUuid(String uuid);

    Page<ClubResponseDto> getAllClubs(Pageable pageable);

    Response<ClubResponseDto> getClubByUuid(String uuid);

    Response<ClubResponseDto> getClubByName(String name);

    Page<EventResponseDto> getEventsByClubUuid(String uuid, Pageable pageable);
}
