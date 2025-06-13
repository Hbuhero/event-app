package hud.app.event_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "event")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@SQLDelete(sql = "UPDATE events SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Event extends BaseEntity implements Serializable {
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "hosted_by", nullable = false)
    private String hostedBy;

    @Column(name = "hosted_url")
    private String hostUrl;

    @Column(name = "start_date", nullable = false)
    private LocalDate startingDate;

    @Column(name = "start_time")
    private LocalTime startingTime;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "about", nullable = false)
    private String about;

    @ManyToOne
    @JoinColumn(name = "type", nullable = false, referencedColumnName = "id")
    private EventType type;

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id", nullable = false)
    private Category category;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "pic_url")
    private String picUrl;

//    @ManyToOne
//    @JoinColumn(name = "club")
//    private Club club;

}
