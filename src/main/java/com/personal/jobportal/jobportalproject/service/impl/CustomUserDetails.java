package com.personal.jobportal.jobportalproject.service.impl;
import com.personal.jobportal.jobportalproject.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class CustomUserDetails extends Users implements UserDetails {

    private final String username;
    private final String password;
    private final int userId;
    private final Integer role;
    private final String email;

    public CustomUserDetails(Users byUsername) {
        this.username = byUsername.getUsername();
        this.password= byUsername.getPassword();
        this.userId = byUsername.getUserId();
        this.role = byUsername.getRole();
        this.email = byUsername.getEmail();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
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
    public int getUserId() {
        return userId;
    }
    @Override
    public Integer getRole() {
        return role;
    }
    @Override
    public String getEmail() {
        return email;
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
