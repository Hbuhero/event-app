package hud.app.event_management.userDetailService;

import hud.app.event_management.model.UserAccount;
import hud.app.event_management.utils.Roles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final String userType;
    private final String password;
    private final String uuid;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    private Roles role;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String phone, String uuid, String firstName, String lastName,
                           String password, Roles userType, Collection<? extends GrantedAuthority> authorities,
                           boolean enabled, boolean credentialsNonExpired, boolean accountNonExpired, boolean accountNonLocked) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userType = String.valueOf(userType);
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public static UserDetailsImpl build(UserAccount user){
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
                authorities,
                user.isEnabled(),
                user.isCredentialsNonExpired(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked()
        );
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
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
