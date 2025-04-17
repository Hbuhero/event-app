package hud.app.event_management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "event_category")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE event_category SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Category extends BaseEntity implements Serializable {
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Event> events;

    @Column(name="image" ) // todo: make this not nullable
    private String categoryImage;

    @Column(name="svg", columnDefinition = "text") // todo: make this not nullable
    private String categorySvg;

    public Category(String name){
        this.name = name;
    }
}
