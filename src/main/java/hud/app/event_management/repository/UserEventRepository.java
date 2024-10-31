package hud.app.event_management.repository;

import hud.app.event_management.model.Event;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.model.UserEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    Optional<UserEvent> findFirstByUuid(String uuid);

    Page<UserEvent> findByUserAccount(UserAccount userAccount, Pageable pageable);
}
