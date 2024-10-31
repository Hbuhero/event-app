package hud.app.event_management.controller;

import hud.app.event_management.dto.request.ClubRequestDto;
import hud.app.event_management.dto.response.ClubResponseDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.model.Club;
import hud.app.event_management.model.Event;
import hud.app.event_management.service.ClubService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.paginationUtils.PageableConfig;
import hud.app.event_management.utils.paginationUtils.PageableParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/club")
public class ClubController {
    private final ClubService clubService;
    private PageableConfig pageableConfig;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping("/create-update")
    private Response<ClubResponseDto> createUpdateClub(@RequestBody ClubRequestDto clubRequestDto){
        return clubService.createUpdateClub(clubRequestDto);
    }

    @PostMapping("/delete/{uuid}")
    private Response<String> deleteByUuid(@PathVariable("uuid") String uuid){
        return clubService.deleteByUuid(uuid);
    }

    @GetMapping("/all")
    private Page<ClubResponseDto> getAllClubs(@RequestBody PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return clubService.getAllClubs(pageable);
    }

    @GetMapping("/{uuid}")
    private Response<ClubResponseDto> getClubByUuid(@PathVariable("uuid") String uuid){
        return clubService.getClubByUuid(uuid);
    }

    @GetMapping("/{name}")
    private Response<ClubResponseDto> getClubByName(@PathVariable("name") String name){
        return clubService.getClubByName(name);
    }

    @GetMapping("/events/{uuid}")
    private Page<EventResponseDto> getEventsByClubUuid(@PathVariable("uuid") String uuid, @RequestBody PageableParam pageableParam){
        Pageable pageable = pageableConfig.pageable(pageableParam);
        return clubService.getEventsByClubUuid(uuid, pageable);
    }
}
