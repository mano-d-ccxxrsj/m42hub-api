package com.m42hub.m42hub_api.abuse.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m42hub.m42hub_api.abuse.dto.response.AbuseStatusResponse;
import com.m42hub.m42hub_api.abuse.mapper.AbuseStatusMapper;
import com.m42hub.m42hub_api.abuse.service.AbuseStatusService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/abuse/status")
@RequiredArgsConstructor
public class AbuseStatusController {

    private final AbuseStatusService service;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('abuse:get-all')")
    public ResponseEntity<List<AbuseStatusResponse>> getAllStatus() {
        return ResponseEntity.ok(service.findAll().stream()
                .map(AbuseStatusMapper::toAbuseStatusResponse)
                .toList());
    }
}