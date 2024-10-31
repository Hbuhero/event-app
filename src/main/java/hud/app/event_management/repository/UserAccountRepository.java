package hud.app.event_management.repository;

import hud.app.event_management.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findFirstByUsername(String username);

    Optional<UserAccount> findFirstByPhone(String phone);

    Optional<UserAccount> findFirstByUuid(String uuid);
}
