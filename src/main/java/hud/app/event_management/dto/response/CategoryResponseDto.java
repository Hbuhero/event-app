package hud.app.event_management.dto.response;

import lombok.Builder;

@Builder
public class CategoryResponseDto {
    private String category;
    private String uuid;
    private long eventCount;
}
