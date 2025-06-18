package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.ClubRequest;
import hud.app.event_management.dto.response.ClubResponseDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.mappers.ClubMapper;
import hud.app.event_management.mappers.EventMapper;
import hud.app.event_management.model.Club;
import hud.app.event_management.repository.ClubRepository;
import hud.app.event_management.repository.EventRepository;
import hud.app.event_management.service.ClubService;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.responseUtils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ClubServiceImpl implements ClubService {

    private final ClubRepository clubRepository;
    private final EventRepository eventRepository;
    private final ClubMapper clubMapper;
    private final EventMapper eventMapper;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository, EventRepository eventRepository, ClubMapper clubMapper, EventMapper eventMapper) {
        this.clubRepository = clubRepository;
        this.eventRepository = eventRepository;
        this.clubMapper = clubMapper;
        this.eventMapper = eventMapper;
    }

    @Override
    public Response<ClubResponseDto> createUpdateClub(ClubRequest clubRequest) {
        return null;
    }

    @Override
    public Response<String> deleteByUuid(String uuid) {
        try {
            if (uuid == null){
                return new Response<>(true, "Argument can not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Club> optionalClub = clubRepository.findFirstByUuid(uuid);
            if (optionalClub.isEmpty()){
                return new Response<>(true, "No record found", ResponseCode.NO_RECORD_FOUND);
            }

            clubRepository.delete(optionalClub.get());
            return new Response<>(false, ResponseCode.SUCCESS, "Deleted successfully");
        } catch (Exception e) {
            return new Response<>(true, "Failed to delete club by uuid with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Page<ClubResponseDto> getAllClubs(Pageable pageable) {
        try {
            return clubRepository.findAll(pageable).map(clubMapper::toDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageImpl<>(new ArrayList<>());
    }

    @Override
    public Response<ClubResponseDto> getClubByUuid(String uuid) {
        try {
            if (uuid == null){
                return new Response<>(true, "Argument can not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Club> optionalClub = clubRepository.findFirstByUuid(uuid);
            if (optionalClub.isEmpty()){
                return new Response<>(true, "No record found", ResponseCode.NO_RECORD_FOUND);
            }

            ClubResponseDto clubResponseDto = clubMapper.toDto(optionalClub.get());
            return new Response<>(false, ResponseCode.SUCCESS, clubResponseDto);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get club by uuid with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<ClubResponseDto> getClubByName(String name) {
        try {
            if (name == null){
                return new Response<>(true, "Argument can not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Club> optionalClub = clubRepository.findFirstByName(name);
            if (optionalClub.isEmpty()){
                return new Response<>(true, "No record found", ResponseCode.NO_RECORD_FOUND);
            }

            ClubResponseDto clubResponseDto = clubMapper.toDto(optionalClub.get());
            return new Response<>(false, ResponseCode.SUCCESS, clubResponseDto);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get club by name with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Page<EventResponseDto> getEventsByClubUuid(String uuid, Pageable pageable) {
        try {
//            // todo: handle expected behavior
//            Optional<Club> optionalClub = clubRepository.findFirstByUuid(uuid);
//            Club club = optionalClub.get();
//            return eventRepository.findAllByClub(club, pageable).map(eventMapper::eventToDto);

            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageImpl<>(new ArrayList<>());
    }
}
