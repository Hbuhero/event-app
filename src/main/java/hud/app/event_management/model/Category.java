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
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE event_category SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Category extends BaseEntity implements Serializable {
    @Column(name = "category", nullable = false, unique = true)
    private String category;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Event> events;

    public Category(String category){
        this.category = category;
    }
}
