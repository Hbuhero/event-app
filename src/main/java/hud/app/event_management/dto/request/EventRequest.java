package hud.app.event_management.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class EventRequest {
    private String title;
    private String about;
    private String hostedBy;
    private String hostUrl;
    private String location;
    private String type;
    private String category;
    private String url;
    private String picUrl;
    private LocalDate startDate;
    private LocalTime startTime;

}
