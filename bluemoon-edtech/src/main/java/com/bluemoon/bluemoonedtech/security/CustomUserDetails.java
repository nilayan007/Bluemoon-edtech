package com.bluemoon.bluemoonedtech.security;

import lombok.Getter;
import com.bluemoon.bluemoonedtech.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;


@Getter
public class CustomUserDetails implements UserDetails {
    private final User user;


    public CustomUserDetails(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
// simple role mapping
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }


    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }


    @Override
    public String getUsername() {
// IMPORTANT: this must match how you generate token subject and how the filter calls loadUserByUsername
// we will use user's publicId (UUID). If you prefer email, change token generation and usages accordingly.
        return user.getPublicId();
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
        return Boolean.TRUE.equals(user.getIsVerified());
    }
}