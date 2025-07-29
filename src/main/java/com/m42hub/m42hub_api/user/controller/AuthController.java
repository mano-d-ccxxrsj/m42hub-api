package com.m42hub.m42hub_api.user.controller;

import com.m42hub.m42hub_api.config.TokenService;
import com.m42hub.m42hub_api.exceptions.UsernameOrPasswordInvalidException;
import com.m42hub.m42hub_api.user.dto.request.LoginRequest;
import com.m42hub.m42hub_api.user.dto.request.UserRequest;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import com.m42hub.m42hub_api.user.dto.response.LoginResponse;
import com.m42hub.m42hub_api.user.dto.response.UserResponse;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
import com.m42hub.m42hub_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {


    @Value("${m42hub.security.cookie-secure}")
    private boolean cookieSecure;

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {

            UsernamePasswordAuthenticationToken usernameAndPassword = new UsernamePasswordAuthenticationToken(request.username(), request.password());
            Authentication authentication = authenticationManager.authenticate(usernameAndPassword);

            User user = (User) authentication.getPrincipal();

            String token = tokenService.generateToken(user);

            ResponseCookie cookie = tokenService.generateCookie(token);

            AuthenticatedUserResponse userResponse = UserMapper.toAuthenticatedUserResponse(user);

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new LoginResponse(userResponse));


        } catch (BadCredentialsException exception) {
            throw new UsernameOrPasswordInvalidException("Usuário ou senha inválido.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticatedUserResponse> save(@RequestBody UserRequest request) {
        User newUser = UserMapper.toUser(request);
        User savedUser = userService.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toAuthenticatedUserResponse(savedUser));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("session", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }


}

