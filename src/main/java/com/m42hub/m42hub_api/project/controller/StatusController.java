package com.m42hub.m42hub_api.project.controller;

import com.m42hub.m42hub_api.project.dto.request.StatusRequest;
import com.m42hub.m42hub_api.project.dto.response.StatusResponse;
import com.m42hub.m42hub_api.project.entity.Status;
import com.m42hub.m42hub_api.project.mapper.StatusMapper;
import com.m42hub.m42hub_api.project.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project/status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusService statusService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('status:get_all')")
    public ResponseEntity<List<StatusResponse>> getAll() {
        return ResponseEntity.ok(statusService.findAll()
                .stream()
                .map(StatusMapper::toStatusResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('status:get_by_id')")
    public ResponseEntity<StatusResponse> getById(@PathVariable Long id) {
        return statusService.findById(id)
                .map(status -> ResponseEntity.ok(StatusMapper.toStatusResponse(status)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('status:create')")
    public ResponseEntity<StatusResponse> save(@RequestBody StatusRequest request) {
        Status newStatus = StatusMapper.toStatus(request);
        Status savedStatus = statusService.save(newStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(StatusMapper.toStatusResponse(savedStatus));
    }


}
