package com.m42hub.m42hub_api.config;

import com.m42hub.m42hub_api.abuse.service.ProfanityService;
import com.m42hub.m42hub_api.exceptions.UnauthorizedException;
import com.m42hub.m42hub_api.user.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final ProfanityService profanityService;
    private final TokenService tokenService;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        String token = null;
        Long userId = null;

        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("session".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            Optional<JWTUserData> optJWTUserData = tokenService.verifyToken(token);
            if (optJWTUserData.isPresent()) {
                JWTUserData userData = optJWTUserData.get();
                userId = userData.id();

                UserDetails userDetails = authService.loadUserByUsername(userData.username());

                if (!userData.username().equals(userDetails.getUsername())) {
                    throw new UnauthorizedException("Token inválido");
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userData, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        byte[] buf = wrappedRequest.getContentAsByteArray();
        if (buf.length > 0) {
            String payload = new String(buf, wrappedRequest.getCharacterEncoding());
            String method = wrappedRequest.getMethod();
            String endpoint = wrappedRequest.getRequestURI();

            try {
                profanityService.validate(payload, userId, method, endpoint);
            } catch (ResponseStatusException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write("Ação não permitida pois viola nossas regras de conduta");
                return;
            }
        }

        filterChain.doFilter(wrappedRequest, response);
    }
}