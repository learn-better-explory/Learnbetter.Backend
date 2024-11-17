package com.learnbetter.LearnBetterApi.data;

import com.learnbetter.LearnBetterApi.data.db.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {

    private User user;
    public long userNumber;

    public UserPrincipal(User user){
        this.user = user;
        this.userNumber = user.getId();
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
}
