package com.m42hub.m42hub_api.user.service;

import com.m42hub.m42hub_api.config.JWTUserData;
import com.m42hub.m42hub_api.exceptions.UnauthorizedException;
import com.m42hub.m42hub_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuário ou senha inválido, tente novamente!"));
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
