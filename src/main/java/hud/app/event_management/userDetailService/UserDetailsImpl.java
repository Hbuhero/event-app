package hud.app.event_management.userDetailService;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.security.Roles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    @Getter
    private final Long id;
    private final String username;
    @Getter
    private final String phone;
    @Getter
    private final String firstName;
    @Getter
    private final String lastName;
    @Getter
    private final String uuid;
    @JsonIgnore
    private final String password;
    private final String userType;

    @Enumerated(EnumType.STRING)
    private Roles role;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String phone, String uuid, String firstName, String lastName,
                           String password, Roles userType, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userType = String.valueOf(userType);
        this.authorities = authorities;
    }

    // Change the build method to non-static
    public static UserDetailsImpl build(UserAccount user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getUserType()));
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPhone(),
                user.getUuid(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                Roles.valueOf(user.getUserType()), // Assuming Role is an enum
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
