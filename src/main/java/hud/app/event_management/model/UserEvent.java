package hud.app.event_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;

@Entity
@Table(name = "user_events")
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user_events SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class UserEvent extends BaseEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name = "userAccount")
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;

    private Boolean notify = userAccount.getNotify();

}
