package com.japim.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // A sua entidade JPA

import com.japim.models.User;

public class AuthSecurity implements UserDetails {

    private final User userEntity;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthSecurity(User userEntity, Collection<? extends GrantedAuthority> authorities) {
        this.userEntity = userEntity;
        this.authorities = authorities;
    }

    public User getUserEntity() {
        return userEntity;
    }

    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}