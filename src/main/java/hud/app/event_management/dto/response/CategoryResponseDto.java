package hud.app.event_management.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryResponseDto {
    private String name;
    private String uuid;
    private long eventCount;
    private String image;
    private String svg;
    private String description;
}
