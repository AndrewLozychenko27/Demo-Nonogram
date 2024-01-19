package ua.lozychenko.nonogram.data.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    PLAYER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}