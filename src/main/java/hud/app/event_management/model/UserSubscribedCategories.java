package hud.app.event_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity(name = "user_subscribed_category")
@SQLDelete(sql = "UPDATE user_accounts SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class UserSubscribedCategories extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "user_account")
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "categpry")
    private Category category;
}
