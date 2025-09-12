package com.m42hub.m42hub_api.user.controller;

import com.m42hub.m42hub_api.config.JWTUserData;
import com.m42hub.m42hub_api.user.dto.request.UserInfoRequest;
import com.m42hub.m42hub_api.user.dto.request.UserPasswordChangeRequest;
import com.m42hub.m42hub_api.user.dto.request.UserRequest;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import com.m42hub.m42hub_api.user.dto.response.UserResponse;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
import com.m42hub.m42hub_api.user.service.AuthService;
import com.m42hub.m42hub_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:get_all')")
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userService.findAll()
                .stream()
                .map(UserMapper::toUserResponse)
                .toList());
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:get_by_username')")
    public ResponseEntity<UserInfoResponse> getByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(UserMapper.toUserInfoResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:get_by_id')")
    public ResponseEntity<AuthenticatedUserResponse> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(UserMapper.toAuthenticatedUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedUserResponse> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return userService.findById(userData.id())
                .map(user -> ResponseEntity.ok(UserMapper.toAuthenticatedUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:create')")
    public ResponseEntity<UserResponse> save(@RequestBody UserRequest request) {
        User newUser = UserMapper.toUser(request);
        User savedUser = userService.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserResponse(savedUser));
    }

    @PatchMapping("/info/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:edit-info')")
    public ResponseEntity<UserInfoResponse> editInfo(@PathVariable String username, @RequestBody UserInfoRequest request) {
        JWTUserData userData = authService.validateUserAccess(username);

        return userService.editInfo(request, userData.id())
                .map(user -> ResponseEntity.ok(UserMapper.toUserInfoResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/profile-pic/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:change-profile-pic')")
    public ResponseEntity<UserInfoResponse> changeProfilePic(@PathVariable String username, @RequestParam("file") MultipartFile file) {

        JWTUserData userData = authService.validateUserAccess(username);

        return userService.changeProfilePic(file, userData.id())
                .map(user -> ResponseEntity.ok(UserMapper.toUserInfoResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/profile-banner/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:change-profile-banner')")
    public ResponseEntity<UserInfoResponse> changeProfileBanner(@PathVariable String username, @RequestParam("file") MultipartFile file) {

        JWTUserData userData = authService.validateUserAccess(username);

        return userService.changeProfileBanner(file, userData.id())
                .map(user -> ResponseEntity.ok(UserMapper.toUserInfoResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }


    @PatchMapping("/password/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:change-password')")
    public ResponseEntity<UserInfoResponse> changePassword(@PathVariable String username, @RequestBody UserPasswordChangeRequest request) {
        JWTUserData userData = authService.validateUserAccess(username);

        ResponseCookie cookie = ResponseCookie.from("session", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return userService.changePassword(request, userData.id())
                .map(user -> ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(UserMapper.toUserInfoResponse(user))
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("deactivate/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:self-deactivate')")
    public ResponseEntity<UserResponse> deactivate(@PathVariable String username) {
        JWTUserData userData = authService.validateUserAccess(username);

        ResponseCookie cookie = ResponseCookie.from("session", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return userService.changeStatus(userData.id(), false)
                .map(user -> ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(UserMapper.toUserResponse(user))
                )
                .orElse(ResponseEntity.notFound().build());
    }
}
