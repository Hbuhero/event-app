package hud.app.event_management.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class EventRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String about;
    @NotBlank
    private String hostedBy;
    @NotBlank
    private String hostUrl;
    @NotBlank
    private String location;
    @NotBlank
    private String type;
    @NotBlank
    private String category;
    @NotBlank
    private String url;
    private String picUrl;
    @NotBlank
    private LocalDate startDate;
    @NotBlank
    private LocalTime startTime;

}
