package com.m42hub.m42hub_api.abuse.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.m42hub.m42hub_api.abuse.dto.request.AbuseRequest;
import com.m42hub.m42hub_api.abuse.dto.response.AbuseResponse;
import com.m42hub.m42hub_api.abuse.enums.TargetTypeAbuseEnum;
import com.m42hub.m42hub_api.abuse.mapper.AbuseMapper;
import com.m42hub.m42hub_api.abuse.service.AbuseService;
import com.m42hub.m42hub_api.config.JWTUserData;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/abuse")
@RequiredArgsConstructor
public class AbuseController {

    private final AbuseService abuseService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('abuse:create')")
    public ResponseEntity<AbuseResponse> createAbuse(@Valid @RequestBody AbuseRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AbuseMapper.toAbuseResponse(abuseService.createAbuse(AbuseMapper.toAbuse(request), userData.id(),
                        request.reasonCategoryId())));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('abuse:get-all')")
    public ResponseEntity<Page<AbuseResponse>> getAllAbuses(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) List<String> status,
            @RequestParam(required = false) List<TargetTypeAbuseEnum> targetType,
            @RequestParam(required = false) List<Long> reasonCategory,
            @RequestParam(required = false) Long reporterId,
            @RequestParam(required = false) Long targetId) {

        Page<AbuseResponse> abuses = abuseService.findByParams(
                page, limit, sortBy, sortDirection, status, targetType,
                reasonCategory, reporterId, targetId)
                .map(AbuseMapper::toAbuseResponse);

        return ResponseEntity.ok(abuses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('abuse:get-one')")
    public ResponseEntity<AbuseResponse> getAbuseById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(AbuseMapper.toAbuseResponse(abuseService.findById(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('abuse:update')")
    public ResponseEntity<AbuseResponse> updateStatus(@PathVariable Long id, @RequestParam Long status) {
        return ResponseEntity.ok(AbuseMapper.toAbuseResponse(abuseService.updateStatus(id, status)));
    }
}