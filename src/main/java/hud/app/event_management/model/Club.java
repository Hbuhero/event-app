package hud.app.event_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "club")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE club SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Club extends BaseEntity implements Serializable {
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phone;

    @Column(name = "profile_picture", unique = true, nullable = false)
    private String picUrl;

    @OneToOne
    @JoinColumn(name = "club_admin")
    private UserAccount clubAdmin;

    @OneToMany(mappedBy = "club", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Event> clubEvents;
}
