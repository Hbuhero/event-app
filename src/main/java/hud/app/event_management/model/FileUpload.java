package hud.app.event_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "file_upload")
@SQLDelete(sql = "UPDATE file_upload SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class FileUpload extends BaseEntity implements Serializable {
    @Column(name = "file_uri", nullable = false)
    private String fileURI;

    @Column(name = "server_path", nullable = false)
    private String serverPath;

    @Column(name = "entity_id", nullable = true)
    private Long entityID;

    @Column(name = "file_namee", nullable = false)
    private String fileName;
}
