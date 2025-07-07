package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.request.EventRequest;
import hud.app.event_management.dto.response.EventResponseDto;
import hud.app.event_management.exceptions.ResourceNotFoundException;
import hud.app.event_management.mappers.EventMapper;
import hud.app.event_management.model.*;
import hud.app.event_management.repository.CategoryRepository;
import hud.app.event_management.repository.EventRepository;
import hud.app.event_management.repository.EventTypeRepository;
import hud.app.event_management.service.EventService;
import hud.app.event_management.utils.FileUtil;
import hud.app.event_management.utils.responseUtils.Response;
import hud.app.event_management.utils.responseUtils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final EventTypeRepository eventTypeRepository;
    private final FileUtil fileUtil;


    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, CategoryRepository categoryRepository, EventTypeRepository eventTypeRepository, FileUtil fileUtil) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.categoryRepository = categoryRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.fileUtil = fileUtil;
    }

    @Override
    public Response<EventResponseDto> getEventByName(String name) {

            if (name == null){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

        System.out.println("inside service "+ name);

            Event event = eventRepository.findFirstByTitle(name).orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        System.out.println("after finding event");
            EventResponseDto eventResponseDto = eventMapper.eventToDto(event);

            return new Response<>(false, ResponseCode.SUCCESS, eventResponseDto);
    }

    @Override
    public Response<EventResponseDto> createUpdateEvent(UserAccount userAccount, EventRequest eventDto) {
        try {

            Optional<EventType> optionalEventType = eventTypeRepository.findByType(eventDto.getType());

            if (optionalEventType.isEmpty()){
                return new Response<>(true, "No event type specified is found", ResponseCode.NO_RECORD_FOUND);
            }

            Optional<Category> optionalCategory = categoryRepository.findFirstByName(eventDto.getCategory());

            if (optionalCategory.isEmpty()){
                return new Response<>(true, "No category specified is found", ResponseCode.NO_RECORD_FOUND);
            }

            Optional<Event> optionalEvent = eventRepository.findFirstByTitle(eventDto.getTitle());

            if (optionalEvent.isEmpty()){

                Event event = eventRepository.save(
                        Event.builder()
                                .title(eventDto.getTitle())
                                .hostedBy(eventDto.getHostedBy())
                                .hostUrl(eventDto.getHostUrl())
                                .location(eventDto.getLocation())
                                .about(eventDto.getAbout())
                                .type(optionalEventType.get())
                                .category(optionalCategory.get())
                                .status(EventStatus.UPCOMING)
                                .url(eventDto.getUrl())
                                .picUrl(eventDto.getEventPic())
                                .startingDate(eventDto.getStartDate())
                                .startingTime(eventDto.getStartTime())
                                .build()
                );

                return new Response<>(false, "Successfully added a new event", ResponseCode.SUCCESS, eventMapper.eventToDto(event));
            }else {

                if (userAccount == null){
                    return new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
                }

                if (!userAccount.getUserType().equals( "SUPER_ADMIN")){
                    return new Response<>(true, "Updating request need super admin restrict access", ResponseCode.UNAUTHORIZED);
                }

                Event event = optionalEvent.get();

                Optional<FileUpload> optionalFileUpload = fileUtil.findByPath(event.getPicUrl());
                optionalFileUpload.ifPresent(fileUtil::setFileStatusIsDeleted);

                event.setTitle(eventDto.getTitle());
                event.setHostedBy(eventDto.getHostedBy());
                event.setHostUrl(eventDto.getHostUrl());
                event.setStartingDate(eventDto.getStartDate());
                event.setStartingTime(eventDto.getStartTime());
                event.setLocation(eventDto.getLocation());
                event.setAbout(eventDto.getAbout());
                event.setType(optionalEventType.get());
                event.setCategory(optionalCategory.get());
                event.setUrl(eventDto.getUrl());
                event.setPicUrl(eventDto.getEventPic());

                EventResponseDto  responseDto = eventMapper.eventToDto(eventRepository.save(event));
                return new Response<>(false, "Successfully updated event", ResponseCode.SUCCESS, responseDto);
            }

        } catch (Exception e) {
            return new Response<>(true, "Failed to create/update event with root cause: \n" + e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<String> deleteEventByUuid(String uuid) {
        try {
            if (uuid == null) {
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Event> optionalEvent = eventRepository.findFirstByUuid(uuid);
            if (optionalEvent.isEmpty()) {
                return new Response<>(true, "Event not found", ResponseCode.NO_RECORD_FOUND);
            }

            Event event = optionalEvent.get();

            Optional<FileUpload> optionalFileUpload = fileUtil.findByPath(event.getPicUrl());
            optionalFileUpload.ifPresent(fileUtil::setFileStatusIsDeleted);

            eventRepository.deleteById(event.getId());

            return new Response<>(false, ResponseCode.SUCCESS, "Event deleted successfully");
        } catch (Exception e) {
            return new Response<>(true, "Failed to delete event with cause: \n", ResponseCode.FAIL);
        }
    }

    @Override
    public Response<?> getAllEvents(Pageable pageable) {
        try {
            Page<EventResponseDto> eventResponse = eventRepository.findAll(pageable).map(eventMapper::eventToDto);
            return new Response<>(false, ResponseCode.SUCCESS, eventResponse);
        }catch (Exception e){
            return new Response<>(true, "Failed to get all events with cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<?> getEventsByCategoryUuid(String uuid, Pageable pageable) {
        try {

            if (uuid == null){
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }

            Optional<Category> optionalCategory = categoryRepository.findFirstByUuid(uuid);
            if (optionalCategory.isEmpty()){
                return new Response<>(true, "No category found", ResponseCode.NO_RECORD_FOUND);
            }

//            eventRepository.
            Page<EventResponseDto> eventPage = eventRepository.findAllByCategory(optionalCategory.get(), pageable).map(eventMapper::eventToDto);

            return new Response<>(false, ResponseCode.SUCCESS, eventPage);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get events by category uuid:" + uuid +" with root cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }

    }

    @Override
    public Response<Page<EventResponseDto>> getRandomEvents(Pageable pageable) {
        try {
            int limit = 7;
            List<EventResponseDto> event = eventRepository.findRandom(limit).stream().map(eventMapper::eventToDto).toList();
            Page<EventResponseDto> eventPage = new PageImpl<>(event);
            return new Response<>(false, ResponseCode.SUCCESS, eventPage);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get random upcoming events with cause:\n"+ e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<EventResponseDto> getEventByUuid(String uuid) {
        try {
            if (uuid == null) {
                return new Response<>(true, "Argument should not be null", ResponseCode.NULL_ARGUMENT);
            }
            System.out.printf(uuid);
            Optional<Event> optionalEvent = eventRepository.findByUuid(uuid);
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
