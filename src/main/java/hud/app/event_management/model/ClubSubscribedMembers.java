package hud.app.event_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "club_subscribed_members")
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE club_subscribed_members SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class ClubSubscribedMembers extends BaseEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name = "club")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "subscribed_users")
    private UserAccount userAccount;
}
