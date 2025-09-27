package com.m42hub.m42hub_api.contribution.controller;

import com.m42hub.m42hub_api.contribution.dto.request.StatusRequest;
import com.m42hub.m42hub_api.contribution.dto.response.StatusResponse;
import com.m42hub.m42hub_api.contribution.entity.Status;
import com.m42hub.m42hub_api.contribution.mapper.StatusMapper;
import com.m42hub.m42hub_api.contribution.service.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("contributionStatusController")
@RequestMapping("/api/v1/contribution/status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution_status:create')")
    public ResponseEntity<List<StatusResponse>> getAll() {
        return ResponseEntity.ok(statusService.findAll()
                .stream()
                .map(StatusMapper::toStatusResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution_status:get_by_id')")
    public ResponseEntity<StatusResponse> getById(@PathVariable Long id) {
        return statusService.findById(id)
                .map(status -> ResponseEntity.ok(StatusMapper.toStatusResponse(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution_status:create')")
    public ResponseEntity<StatusResponse> save(@RequestBody @Valid StatusRequest request) {
        Status newStatus = StatusMapper.toStatus(request);
        Status savedStatus = statusService.save(newStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(StatusMapper.toStatusResponse(savedStatus));
    }


}
