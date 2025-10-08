package com.m42hub.m42hub_api.user.service;

import com.m42hub.m42hub_api.config.JWTUserData;
import com.m42hub.m42hub_api.exceptions.UnauthorizedException;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.entity.UserDetailsImpl;
import com.m42hub.m42hub_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SystemRoleService systemRoleService;
    private final PermissionService permissionService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        SystemRole systemRole = systemRoleService.findById(user.getSystemRoleId())
                .orElseThrow(() -> new RuntimeException("SystemRole não encontrado"));

        List<Permission> permissions = permissionService.findBySystemRoleId(systemRole.getId());

        return new UserDetailsImpl(user, systemRole, permissions);
    }

    public JWTUserData validateUserAccess(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        if (!Objects.equals(userData.username(), username)) {
            throw new UnauthorizedException("Usuário não tem permissão para realizar esta ação");
        }

        return userData;
    }
}