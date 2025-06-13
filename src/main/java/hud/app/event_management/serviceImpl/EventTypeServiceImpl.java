package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.dto.response.EventTypeResponseDto;
import hud.app.event_management.mappers.EventMapper;
import hud.app.event_management.mappers.EventTypeMapper;
import hud.app.event_management.model.Event;
import hud.app.event_management.model.EventType;
import hud.app.event_management.repository.EventRepository;
import hud.app.event_management.repository.EventTypeRepository;
import hud.app.event_management.service.EventTypeService;
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
public class EventTypeServiceImpl implements EventTypeService {
    private final EventTypeRepository eventTypeRepository;
    private final EventRepository eventRepository;
    private final EventTypeMapper eventTypeMapper;
    private final EventMapper eventMapper;

    @Autowired
    public EventTypeServiceImpl(EventTypeRepository eventTypeRepository, EventRepository eventRepository, EventTypeMapper eventTypeMapper, EventMapper eventMapper) {
        this.eventTypeRepository = eventTypeRepository;
        this.eventRepository = eventRepository;
        this.eventTypeMapper = eventTypeMapper;
        this.eventMapper = eventMapper;
    }

    @Override
    public Page<EventTypeResponseDto> getAllEventTypes(Pageable pageable) {
        try {
            return eventTypeRepository.findAll(pageable).map(eventTypeMapper::toDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageImpl<>(new ArrayList<>());
    }

    @Override
    public Response<EventTypeResponseDto> getByUuid(String uuid) {
        try {
            if (uuid == null){
                return new Response<>(true, "Argument can not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<EventType> optionalEventType = eventTypeRepository.findFirstByUuid(uuid);
            if (optionalEventType.isEmpty()){
                return new Response<>(true, "Record not found", ResponseCode.NO_RECORD_FOUND);
            }
            EventTypeResponseDto responseDto = eventTypeMapper.toDto(optionalEventType.get());
             return new Response<>(false, ResponseCode.SUCCESS, responseDto);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get event by uuid with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Page<EventResponseDto> getEventsByType(String uuid, Pageable pageable) {
        try {
            // todo: handle errors
            Optional<EventType> optionalEventType = eventTypeRepository.findFirstByUuid(uuid);
            EventType eventType = optionalEventType.get();
            return eventRepository.findAllByType(eventType, pageable).map(eventMapper::eventToDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PageImpl<>(new ArrayList<>());
    }

    @Override
    public Response<String> deleteByUuid(String uuid) {
        try {
            if (uuid == null) {
                return new Response<>(true, "Argument can not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<EventType> optionalEventType = eventTypeRepository.findFirstByUuid(uuid);
            if (optionalEventType.isEmpty()) {
                return new Response<>(true, "Record not found", ResponseCode.NO_RECORD_FOUND);
            }

            eventTypeRepository.delete(optionalEventType.get());

            return new Response<>(false, ResponseCode.SUCCESS, "Deleted successfully");
        } catch (Exception e) {
            return new Response<>(true, "Failed to delete event type by uuid with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<EventTypeResponseDto> createUpdateEventType(String type) {
        try {
            Optional<EventType> optionalEventType = eventTypeRepository.findFirstByType(type);
            if (optionalEventType.isPresent()){
                EventType eventType = optionalEventType.get();
                eventType.setType(type);
                EventTypeResponseDto responseDto = eventTypeMapper.toDto(eventTypeRepository.save(eventType));

                return new Response<>(false, "Created event type successfully", ResponseCode.SUCCESS, responseDto);

            }else{
                EventTypeResponseDto eventType = eventTypeMapper.toDto(eventTypeRepository.save(
                        EventType.builder()
                                .type(type)
                                .build()
                ));

                return new Response<>(false, "Created event type successfully", ResponseCode.SUCCESS, eventType);
            }
        } catch (Exception e) {
            return new Response<>(true, "Failed to create/update event type with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }
}
