package hud.app.event_management.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EventTypeResponseDto {
    private String uuid;
    private String type;
    private long eventCount;
}
