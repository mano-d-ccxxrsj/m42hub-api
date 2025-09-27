package com.m42hub.m42hub_api.donation.controller;

import com.m42hub.m42hub_api.donation.dto.request.PlatformRequest;
import com.m42hub.m42hub_api.donation.dto.response.PlatformResponse;
import com.m42hub.m42hub_api.donation.entity.Platform;
import com.m42hub.m42hub_api.donation.mapper.PlatformMapper;
import com.m42hub.m42hub_api.donation.service.PlatformService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("donationPlatformController")
@RequestMapping("/api/v1/donation/platform")
@RequiredArgsConstructor
public class PlatformController {

    private final PlatformService platformService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution_platform:create')")
    public ResponseEntity<List<PlatformResponse>> getAll() {
        return ResponseEntity.ok(platformService.findAll()
                .stream()
                .map(PlatformMapper::toPlatformResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution_platform:get_by_id')")
    public ResponseEntity<PlatformResponse> getById(@PathVariable Long id) {
        return platformService.findById(id)
                .map(platform -> ResponseEntity.ok(PlatformMapper.toPlatformResponse(platform)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution_platform:create')")
    public ResponseEntity<PlatformResponse> save(@RequestBody @Valid PlatformRequest request) {
        Platform newPlatform = PlatformMapper.toPlatform(request);
        Platform savedPlatform = platformService.save(newPlatform);
        return ResponseEntity.status(HttpStatus.CREATED).body(PlatformMapper.toPlatformResponse(savedPlatform));
    }


}
