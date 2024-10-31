package hud.app.event_management.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity(name = "user_accounts")
@SQLDelete(sql = "UPDATE user_accounts SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class UserAccount extends BaseEntity implements Serializable {

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "middlename")
    private String middleName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true)
    private String username;

    // @Pattern(regexp = "(^(([2]{1}[5]{2})|([0]{1}))[1-9]{2}[0-9]{7}$)", message = "Please enter valid phone number eg. 255614157633")
    @Column(name = "phone_number", unique = true)
    private String phone;

    @Column(name = "userType")
    private String userType;

    @Column(name = "address")
    private String address;

    @JsonIgnore
    private String password;

    @Column(name = "profile_photo")
    private String profilePhoto;

//    @JsonIgnore
//    @Basic(optional = true)
//    @Column(name = "token_created_at")
//    private LocalDateTime tokenCreatedAt = LocalDateTime.now();
//
//    @JsonIgnore
//    @Basic(optional = true)
//    @Column(name = "last_login")
//    private LocalDateTime lastLogin;
//
//    @Basic(optional = true)
//    @Column(name = "remember_token")
//    private String rememberToken;
//
    @JsonIgnore
    @Column(name = "otp_sent")
    private String oneTimePassword;

    @JsonIgnore
    @Column(name = "is_otp_verified")
    private boolean isOtpVerified = false;
//
//    @JsonIgnore
//    private int loginAttempts = 0;
//
    @JsonIgnore
    private LocalDateTime lastOtpSentAt;
//
//    @JsonIgnore
//    private LocalDateTime lastLoginAttempt;
//
//    @JsonIgnore
//    @Column(name = "refresh_token")
//    private String refreshToken;
//
//    @JsonIgnore
//    @Column(name = "refresh_token_created_at")
//    private LocalDateTime refreshTokenCreatedAt;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    public String fullName() {
        return this.firstName + (middleName != null ? " " + this.middleName + " " : " ") + this.lastName;
    }

    // public List<String> getPermissions() {
    // List<String> permissions = new ArrayList<>();
    //
    // // Assuming your userRole is a string representing the role
    // if (this.userType == null) {
    // switch (this.userType) {
    // case "CUSTOMER":
    // permissions.add("USER_PERMISSION");
    // break;
    // case "MANAGER":
    // permissions.add("MANAGER_PERMISSION");
    // break;
    // case "SUPER_ADMIN":
    // permissions.add("SUPER_ADMIN_PERMISSION");
    // break;
    // // Add more cases if needed
    // }
    // }
    //
    // return permissions;
    // }

}
