package hud.app.event_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CategoryRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;

    @NotBlank
    private String categoryImage;
    private String categorySvg;
}
