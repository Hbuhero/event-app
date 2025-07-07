package hud.app.event_management.repository;

import hud.app.event_management.model.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileUpload, Long> {
    Optional<FileUpload> findByPath(String path);

}
