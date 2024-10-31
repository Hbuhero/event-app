package hud.app.event_management.repository;

import hud.app.event_management.model.Club;
import hud.app.event_management.model.ClubSubscribedMembers;
import hud.app.event_management.model.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubSubscribedMembersRepository extends JpaRepository<ClubSubscribedMembers, Long> {
    Optional<ClubSubscribedMembers> findByClubAndUserAccount(Club club, UserAccount userAccount);

    Page<ClubSubscribedMembers> findAllByClub(Club club, Pageable pageable);

    Page<ClubSubscribedMembers> findAllByUserAccount(UserAccount userAccount, Pageable pageable);
}
