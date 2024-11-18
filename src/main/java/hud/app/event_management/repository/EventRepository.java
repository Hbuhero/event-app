package hud.app.event_management.repository;

import hud.app.event_management.model.Category;
import hud.app.event_management.model.Club;
import hud.app.event_management.model.Event;
import hud.app.event_management.model.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findFirstByTitle(String name);

    Page<Event> findAllByCategory(Category category, Pageable pageable);

    Page<Event> findAllByType(EventType eventType, Pageable pageable);

    Page<Event> findAllByClub(Club club, Pageable pageable);

    Optional<Event> findByUuid(String uuid);
}
