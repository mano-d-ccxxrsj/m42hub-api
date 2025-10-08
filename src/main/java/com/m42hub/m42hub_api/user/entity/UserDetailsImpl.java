package com.m42hub.m42hub_api.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final SystemRole systemRole;
    private final List<Permission> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions.isEmpty()) {
            return List.of(new SimpleGrantedAuthority("ROLE_" + systemRole.getName()));
        }

        List<GrantedAuthority> authorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + systemRole.getName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername().toLowerCase();
    }

    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE.equals(user.getIsActive());
    }

    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE.equals(user.getIsActive());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE.equals(user.getIsActive());
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getIsActive());
    }
}