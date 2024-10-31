package hud.app.event_management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "event_type")
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE event_type SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class EventType extends BaseEntity implements Serializable {
    @Column(name = "type", nullable = false, unique = true)
    private String type;

    @OneToMany(mappedBy = "type", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Event> events;

    public EventType(String type){
        this.type = type;
    }
}
