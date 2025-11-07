package com.example.jwtexample.security;

import com.example.jwtexample.domain.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends User {
    public CustomUserDetails(UserAccount account) {
        super(account.getUsername(), account.getPassword(), authorities(account));
    }

    private static Collection<? extends GrantedAuthority> authorities(UserAccount account) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()));
    }
}
