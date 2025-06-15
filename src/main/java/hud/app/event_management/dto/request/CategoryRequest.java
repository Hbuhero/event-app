package hud.app.event_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    //TODO: handle file service
    private String categoryImage;
    private String categorySvg;
}
