package com.example.paymybuddy.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private String mail;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String mail, String password, Collection<? extends GrantedAuthority> authorities) {
        this.mail = mail;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() {
        return mail; // On utilise l'email comme identifiant
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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

    public String getEmail() {
        return mail;
    }
}

