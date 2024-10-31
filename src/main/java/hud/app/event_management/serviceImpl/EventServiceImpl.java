package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.EventDto;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.mappers.EventMapper;
import hud.app.event_management.model.Event;
import hud.app.event_management.repository.EventRepository;
import hud.app.event_management.service.EventService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public Response<EventResponseDto> getEventByName(String name) {
        try {
            if (name == null){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Event> optionalEvent = eventRepository.findFirstByTitle(name);
            if (optionalEvent.isEmpty()){
                return new Response<>(true, "Event not found", ResponseCode.NO_RECORD_FOUND);
            }

            EventResponseDto eventResponseDto = eventMapper.eventToDto(optionalEvent.get());

            return new Response<>(false, ResponseCode.SUCCESS, eventResponseDto);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get all events with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<EventResponseDto> createUpdateEvent(EventDto eventDto) {
        // todo: create/update impl
        return null;
    }

    @Override
    public Response<String> deleteEventByUuid(String uuid) {
        try {
            if (uuid == null) {
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Event> optionalEvent = eventRepository.findFirstByTitle(uuid);
            if (optionalEvent.isEmpty()) {
                return new Response<>(true, "Event not found", ResponseCode.NO_RECORD_FOUND);
            }

            eventRepository.delete(optionalEvent.get());

            return new Response<>(false, ResponseCode.SUCCESS, "Event deleted successfully");
        } catch (Exception e) {
            return new Response<>(true, "Failed to delete event with cause: \n", ResponseCode.FAIL);
        }
    }

    @Override
    public Page<EventResponseDto> getAllEvents(Pageable pageable) {
        try {
            return eventRepository.findAll(pageable).map(eventMapper::eventToDto);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new PageImpl<>(new ArrayList<>());
    }

    @Override
    public Response<EventResponseDto> getEventByUuid(String uuid) {
        try {
            if (uuid == null) {
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Event> optionalEvent = eventRepository.findFirstByTitle(uuid);
            if (optionalEvent.isEmpty()) {
                return new Response<>(true, "Event not found", ResponseCode.NO_RECORD_FOUND);
            }

            EventResponseDto eventResponseDto = eventMapper.eventToDto(optionalEvent.get());
            return new Response<>(false, ResponseCode.SUCCESS, eventResponseDto);

        } catch (Exception e) {
            return new Response<>(true, "Failed to get event with root cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }
}
