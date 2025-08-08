package hud.app.event_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_upload")
@SQLDelete(sql = "UPDATE file_upload SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class FileUpload extends BaseEntity implements Serializable {

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "server_path", nullable = false)
    private String serverPath;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private EntityType entityType;
}
