package com.m42hub.m42hub_api.contribution.controller;

import com.m42hub.m42hub_api.contribution.dto.request.ContributionRequest;
import com.m42hub.m42hub_api.contribution.dto.response.ContributionResponse;
import com.m42hub.m42hub_api.contribution.entity.Contribution;
import com.m42hub.m42hub_api.contribution.mapper.ContributionMapper;
import com.m42hub.m42hub_api.contribution.service.ContributionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contribution")
@RequiredArgsConstructor
public class ContributionController {

    private final ContributionService contributionService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution:create')")
    public ResponseEntity<List<ContributionResponse>> getAll() {
        return ResponseEntity.ok(contributionService.findAll()
                .stream()
                .map(ContributionMapper::toContributionResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution:get_by_id')")
    public ResponseEntity<ContributionResponse> getById(@PathVariable UUID id) {
        return contributionService.findById(id)
                .map(contribution -> ResponseEntity.ok(ContributionMapper.toContributionResponse(contribution)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution:create')")
    public ResponseEntity<ContributionResponse> save(@RequestBody @Valid ContributionRequest request) {
        Contribution newContribution = ContributionMapper.toContribution(request);
        Contribution savedContribution = contributionService.save(newContribution);
        return ResponseEntity.status(HttpStatus.CREATED).body(ContributionMapper.toContributionResponse(savedContribution));
    }


}
