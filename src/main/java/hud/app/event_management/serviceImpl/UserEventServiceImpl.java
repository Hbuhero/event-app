package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.UserEventRequestDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.mappers.EventMapper;
import hud.app.event_management.model.Event;
import hud.app.event_management.model.EventStatus;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.model.UserEvent;
import hud.app.event_management.repository.UserAccountRepository;
import hud.app.event_management.repository.UserEventRepository;
import hud.app.event_management.service.UserEventService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.ResponseCode;
import hud.app.event_management.utils.userExtractor.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserEventServiceImpl implements UserEventService {

    private final UserAccountRepository userAccountRepository;
    private final UserEventRepository userEventRepository;
    private final LoggedUser loggedUser;
    private final EventMapper eventMapper;

    @Autowired
    public UserEventServiceImpl(UserAccountRepository userAccountRepository, UserEventRepository userEventRepository, LoggedUser loggedUser, EventMapper eventMapper) {
        this.userAccountRepository = userAccountRepository;
        this.userEventRepository = userEventRepository;
        this.loggedUser = loggedUser;
        this.eventMapper = eventMapper;
    }

    @Override
    public Response<UserEvent> createUpdateUserEvent(UserEventRequestDto userEventRequestDto) {
        return null;
    }

    @Override
    public Response<String> deleteByUuid(String uuid) {
        try {
            if (uuid == null){
                return new Response<>(true, "Argument can not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<UserEvent> optionalUserEvent = userEventRepository.findFirstByUuid(uuid);
            if (optionalUserEvent.isEmpty()){
                return new Response<>(true, "No record found", ResponseCode.NO_RECORD_FOUND);
            }

            userEventRepository.delete(optionalUserEvent.get());
            return new Response<>(false, ResponseCode.SUCCESS, "Deleted successfully");
        } catch (Exception e) {
            return new Response<>(true, "Failed to delete userEvent with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<EventResponseDto> getUpcomingEvents(Pageable pageable) {
        try {
            UserAccount userAccount = loggedUser.getUser();
            if (userAccount == null){
                return new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
            }

            Page<UserEvent> userEvents = userEventRepository.findByUserAccount(userAccount, pageable);

            List<EventResponseDto> upcomingUserEvents = userEvents.stream()
                    .map(UserEvent::getEvent)
                    .filter(event -> event.getStatus().equals(EventStatus.UPCOMING))
                    .map(eventMapper::eventToDto)
                    .toList();

            return new Response<EventResponseDto>(false, ResponseCode.SUCCESS, upcomingUserEvents);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get user upcoming events with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<EventResponseDto> getPastEvents(Pageable pageable) {
        try {
            UserAccount userAccount = loggedUser.getUser();
            if (userAccount == null){
                return new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
            }

            Page<UserEvent> userEvents = userEventRepository.findByUserAccount(userAccount, pageable);
            List<EventResponseDto> pastUserEvents = userEvents.stream()
                    .map(UserEvent::getEvent)
                    .filter(event -> event.getStatus().equals(EventStatus.UPCOMING))
                    .map(eventMapper::eventToDto)
                    .toList();

            return new Response<>(false, ResponseCode.SUCCESS, pastUserEvents);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get user upcoming events with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }
}
