package com.learnbetter.LearnBetterApi.data;

import com.learnbetter.LearnBetterApi.data.db.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails, Principal {

    private User user;
    public long id;

    public UserPrincipal(User user){
        this.user = user;
        this.id = user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }


    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.getStatus().isExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getStatus().isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.getStatus().isCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus().isEnabled();
    }

    /**
     * Returns the name of this {@code Principal}.
     *
     * @return the name of this {@code Principal}.
     */
    @Override
    public String getName() {
        return user.getUsername();
    }

    /**
     * Returns {@code true} if the specified subject is implied by this
     * {@code Principal}.
     *
     * @param subject the {@code Subject}
     * @return {@code true} if {@code subject} is non-null and is
     * implied by this {@code Principal}, or false otherwise.
     * @implSpec The default implementation of this method returns {@code true} if
     * {@code subject} is non-null and contains at least one
     * {@code Principal} that is equal to this {@code Principal}.
     *
     * <p>Subclasses may override this with a different implementation, if
     * necessary.
     * @since 1.8
     */
    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
