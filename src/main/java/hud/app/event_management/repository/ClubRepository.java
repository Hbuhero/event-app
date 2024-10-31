package hud.app.event_management.repository;

import hud.app.event_management.model.Club;
import hud.app.event_management.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findFirstByUuid(String uuid);

    Optional<Club> findFirstByName(String name);
}
