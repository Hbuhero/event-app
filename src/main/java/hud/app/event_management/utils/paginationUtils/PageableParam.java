package hud.app.event_management.utils.paginationUtils;

import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableParam {
    private String sortBy;
    private String sortDirection;
    private Integer size;
    private Integer first;

    List<SearchFieldsDto> searchFields;

    public PageableParam(Integer size, Integer first) {
        this.size = size;
        this.first = first;
    }

    public PageableParam(String sortBy, String sortDirection, Integer size, Integer first) {
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
        this.size = size;
        this.first = first;
    }
}
