package com.m42hub.m42hub_api.user.controller;

import com.m42hub.m42hub_api.config.JWTUserData;
import com.m42hub.m42hub_api.exceptions.CustomNotFoundException;
import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.user.dto.request.UserInfoRequest;
import com.m42hub.m42hub_api.user.dto.request.UserPasswordChangeRequest;
import com.m42hub.m42hub_api.user.dto.request.UserRequest;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import com.m42hub.m42hub_api.user.dto.response.SystemRoleResponse;
import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import com.m42hub.m42hub_api.user.dto.response.UserResponse;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.SystemRoleMapper;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
import com.m42hub.m42hub_api.user.service.*;
import jakarta.validation.Valid;
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
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserInterestProjectRoleService userInterestProjectRoleService;
    private final SystemRolePermissionService systemRolePermissionService;
    private final SystemRoleService systemRoleService;
    private final UserService userService;
    private final AuthService authService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:get_all')")
    public ResponseEntity<List<UserResponse>> getAll() {
        List<User> users = userService.findAll();

        List<UUID> systemRoleIds = users.stream().map(User::getSystemRoleId).distinct().toList();
        Map<UUID, SystemRole> systemRoleMap = systemRoleService.findAllByIds(systemRoleIds);
        Map<UUID, List<Permission>> permissionsByRole = systemRolePermissionService
                .findPermissionsBySystemRoleIds(systemRoleIds);

        List<UserResponse> responses = users.stream()
                .map(user -> {
                    SystemRole systemRole = systemRoleMap.get(user.getSystemRoleId());
                    List<Permission> permissions = permissionsByRole.getOrDefault(user.getSystemRoleId(), List.of());
                    SystemRoleResponse systemRoleResponse = SystemRoleMapper.toSystemRoleResponse(systemRole, permissions);
                    return UserMapper.toUserResponse(user, systemRoleResponse);
                })
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:get_by_username')")
    public ResponseEntity<UserInfoResponse> getByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(this.getUserInfoResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:get_by_id')")
    public ResponseEntity<AuthenticatedUserResponse> getById(@PathVariable UUID id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(this.getAuthenticatedUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedUserResponse> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return userService.findById(userData.id())
                .map(user -> ResponseEntity.ok(this.getAuthenticatedUserResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:create')")
    public ResponseEntity<UserResponse> save(@RequestBody @Valid UserRequest request) {
        User newUser = UserMapper.toUser(request);
        User savedUser = userService.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                UserMapper.toUserResponse(savedUser, this.getSystemRoleResponse(savedUser))
        );
    }

    @PatchMapping("/info/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:edit-info')")
    public ResponseEntity<UserInfoResponse> editInfo(@PathVariable String username, @RequestBody @Valid UserInfoRequest request) {
        JWTUserData userData = authService.validateUserAccess(username);

        User user = UserMapper.toUser(request);

        return userService.update(user, userData.id(), request.interestRoles())
                .map(updatedUser -> ResponseEntity.ok(this.getUserInfoResponse(updatedUser)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/profile-pic/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:change-profile-pic')")
    public ResponseEntity<UserInfoResponse> changeProfilePic(@PathVariable String username, @RequestParam("file") MultipartFile file) {

        JWTUserData userData = authService.validateUserAccess(username);

        return userService.changeProfilePic(file, userData.id())
                .map(user -> ResponseEntity.ok(this.getUserInfoResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/profile-banner/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:change-profile-banner')")
    public ResponseEntity<UserInfoResponse> changeProfileBanner(@PathVariable String username, @RequestParam("file") MultipartFile file) {

        JWTUserData userData = authService.validateUserAccess(username);

        return userService.changeProfileBanner(file, userData.id())
                .map(user -> ResponseEntity.ok(this.getUserInfoResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/password/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:change-password')")
    public ResponseEntity<UserInfoResponse> changePassword(@PathVariable String username, @RequestBody @Valid UserPasswordChangeRequest request) {
        JWTUserData userData = authService.validateUserAccess(username);

        ResponseCookie cookie = ResponseCookie.from("session", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return userService.changePassword(request.oldPassword(), request.newPassword(), userData.id())
                .map(user -> ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(this.getUserInfoResponse(user))
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
                .map(user ->
                        ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(UserMapper.toUserResponse(user, this.getSystemRoleResponse(user)))
                ).orElse(ResponseEntity.notFound().build());
    }

    // Métodos auxiliares, para não manchar a classe de Mapper com imports de serviço, e para não manchar a classe de Service com DTOs
    public SystemRoleResponse getSystemRoleResponse(User user) {
        SystemRole systemRole = systemRoleService.findById(user.getSystemRoleId()).orElseThrow(() ->
                new CustomNotFoundException("SystemRole não encontrado")
        );
        List<Permission> permissions = systemRolePermissionService.findPermissionsBySystemRole(user.getSystemRoleId());
        return SystemRoleMapper.toSystemRoleResponse(systemRole, permissions);
    }

    public UserInfoResponse getUserInfoResponse(User user) {
        SystemRole systemRole = systemRoleService.findById(user.getSystemRoleId()).orElseThrow(() ->
                new CustomNotFoundException("SystemRole não encontrado")
        );
        List<Role> interestRoles = userInterestProjectRoleService.getRolesByUser(user.getId());
        return UserMapper.toUserInfoResponse(user, systemRole, interestRoles);
    }

    public AuthenticatedUserResponse getAuthenticatedUserResponse(User user) {
        return systemRoleService.findById(user.getSystemRoleId()).map(
                foundSystemRole -> UserMapper.toAuthenticatedUserResponse(user, foundSystemRole)
        ).orElseThrow(() ->
                new CustomNotFoundException("SystemRole não encontrado")
        );
    }
}