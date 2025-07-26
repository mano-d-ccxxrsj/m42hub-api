package com.m42hub.m42hub_api.config;

import com.m42hub.m42hub_api.exceptions.UnauthorizedException;
import com.m42hub.m42hub_api.user.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (Strings.isNotEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring("Bearer ".length());

            Optional<JWTUserData> optJWTUserData = tokenService.verifyToken(token);
            if (optJWTUserData.isPresent()) {
                JWTUserData userData = optJWTUserData.get();

                UserDetails userDetails = authService.loadUserByUsername(userData.username());

                if (!userData.username().equals(userDetails.getUsername())) {
                    throw new UnauthorizedException("Token inválido");
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userData, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("Authorities: " + authentication.getAuthorities());

            }
        }
        filterChain.doFilter(request, response);
    }

}
