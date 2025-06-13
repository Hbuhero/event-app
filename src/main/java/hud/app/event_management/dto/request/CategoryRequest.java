package hud.app.event_management.dto.request;

import lombok.Getter;

@Getter
public class CategoryRequest {
    private String name;
    private String description;
    private String categoryImage;
    private String categorySvg;
}
