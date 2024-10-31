package hud.app.event_management.utils.paginationUtils;
import lombok.AllArgsConstructor;
import java.util.List;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author developers@PMO-Dashboard
 *
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchFieldsDto {

    public enum SearchOperationType {
        Equals, NotEquals, Like, LessThan, GreaterThan, In
    }

    private String fieldName;
    private Object fieldValue;
    private List<Object> fieldValues;

    @Enumerated(EnumType.STRING)
    private SearchOperationType searchType;
}

