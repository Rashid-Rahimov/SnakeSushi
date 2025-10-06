package com.example.snakesushi.auth;

import com.example.snakesushi.model.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private List<GrantedAuthority> roles = new ArrayList<>();

    public CustomUserDetails(Admin admin) {
        this.id = admin.getId();
        this.username = admin.getNick();
        this.password = admin.getPassword();
        this.roles.add(new SimpleGrantedAuthority("ROLE_" + admin.getRole().name()));
    }

    public Long getId () {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
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
