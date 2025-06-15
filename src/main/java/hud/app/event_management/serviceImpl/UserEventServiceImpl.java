package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.UserEventRequest;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.mappers.EventMapper;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.model.UserEvent;
import hud.app.event_management.repository.UserEventRepository;
import hud.app.event_management.service.UserEventService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserEventServiceImpl implements UserEventService {

    private final UserEventRepository userEventRepository;

    private final EventMapper eventMapper;

    @Autowired
    public UserEventServiceImpl(UserEventRepository userEventRepository, EventMapper eventMapper) {
        this.userEventRepository = userEventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public Response<UserEvent> createUpdateUserEvent(UserAccount userAccount,UserEventRequest userEventRequest) {
        // TODO: implement this
        return null;
    }

    @Override
    public Response<String> deleteByUuid(UserAccount userAccount, String uuid) {
        try {
            if (userAccount == null){
                return  new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
            }
            if (uuid == null){
                return new Response<>(true, "Argument can not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<UserEvent> optionalUserEvent = userEventRepository.findFirstByUuid(uuid);
            if (optionalUserEvent.isEmpty()){
                return new Response<>(true, "No record found", ResponseCode.NO_RECORD_FOUND);
            }

            UserEvent userEvent = optionalUserEvent.get();

            if (!userEvent.getUserAccount().getUuid().equals(userAccount.getUuid())){
                return new Response<>(true, "User event accessed is not of logged user", ResponseCode.INVALID_REQUEST);
            }

            userEventRepository.delete(userEvent);
            return new Response<>(false, ResponseCode.SUCCESS, "Deleted successfully");
        } catch (Exception e) {
            return new Response<>(true, "Failed to delete userEvent with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<?> getAllUserEvents(UserAccount userAccount, Pageable pageable) {
        try {

            if (userAccount == null){
                return new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
            }

            Page<EventResponseDto> userEvents = userEventRepository.findByUserAccount(userAccount, pageable).map(e -> eventMapper.eventToDto(e.getEvent()));

            return new Response<>(false, ResponseCode.SUCCESS, userEvents);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get user upcoming events with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }
}
