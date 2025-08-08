package hud.app.event_management.repository;


import hud.app.event_management.model.Category;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.model.UserSubscribedCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscribedCategoryRepository extends JpaRepository<UserSubscribedCategory, Long> {

    Page<UserSubscribedCategory> findAllCategoryByUserAccount(UserAccount userAccount, Pageable pageable);

    void deleteByUserAccountAndCategory(UserAccount userAccount, Category category);

    Optional<UserSubscribedCategory> findByUserAccountAndCategory(UserAccount userAccount, Category category);

    List<UserSubscribedCategory> findAllByUserAccount(UserAccount userAccount);
}
