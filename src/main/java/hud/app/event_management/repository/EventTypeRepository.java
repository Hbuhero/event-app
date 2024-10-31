package hud.app.event_management.repository;

import hud.app.event_management.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventTypeRepository extends JpaRepository<EventType, Long> {
    Optional<EventType> findFirstByUuid(String uuid);
}
