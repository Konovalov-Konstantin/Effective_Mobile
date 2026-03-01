package com.example.bankcards.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, USER, HACKER;

    @Override
    public String getAuthority() {
        return name();
    }
}
