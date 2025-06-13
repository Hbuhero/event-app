package hud.app.event_management.dto.response;

import hud.app.event_management.model.Category;
import hud.app.event_management.model.EventStatus;
import hud.app.event_management.model.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@Getter
@Setter
public class EventResponseDto implements Serializable {
    private String uuid;
    private String title;
    private String hostedBy;
    private String hostUrl;
    private LocalDate startingDate;
    private LocalTime startingTime;
    private String location;
    private String about;
    private String type;
    private String category;
    private String status;
    private String url;
    private String picUrl;
}
