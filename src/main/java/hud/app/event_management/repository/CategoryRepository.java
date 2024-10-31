package hud.app.event_management.repository;

import hud.app.event_management.model.Category;
import hud.app.event_management.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findFirstByUuid(String uuid);
}
