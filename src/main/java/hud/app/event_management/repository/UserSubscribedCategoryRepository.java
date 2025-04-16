package hud.app.event_management.repository;


import hud.app.event_management.model.Category;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.model.UserSubscribedCategories;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubscribedCategoryRepository extends JpaRepository<UserSubscribedCategories, Long> {
    Page<UserSubscribedCategories> findAllCategoryByUserAccount(UserAccount userAccount, Pageable pageable);

    void deleteByUserAccountAndCategory(UserAccount userAccount, Category category);
}
