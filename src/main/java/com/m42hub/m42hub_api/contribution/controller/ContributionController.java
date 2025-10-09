package com.m42hub.m42hub_api.contribution.controller;

import com.m42hub.m42hub_api.contribution.dto.request.ContributionRequest;
import com.m42hub.m42hub_api.contribution.dto.response.ContributionListItemResponse;
import com.m42hub.m42hub_api.contribution.dto.response.ContributionResponse;
import com.m42hub.m42hub_api.contribution.dto.response.ContributionsByUserResponse;
import com.m42hub.m42hub_api.contribution.entity.Contribution;
import com.m42hub.m42hub_api.contribution.mapper.ContributionMapper;
import com.m42hub.m42hub_api.contribution.service.ContributionService;
import com.m42hub.m42hub_api.shared.dto.PageResponse;
import com.m42hub.m42hub_api.shared.mapper.PageMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution:search')")
    public ResponseEntity<PageResponse<ContributionListItemResponse>> findByParams(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "50", required = false) Integer limit,
            @RequestParam(defaultValue = "approvedAt", required = false) String sortBy,
            @RequestParam(defaultValue = "ASC", required = false) String sortDirection,
            @RequestParam(required = false) List<Long> status,
            @RequestParam(required = false) List<Long> type,
            @RequestParam(required = false) List<Long> user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date submittedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date submittedAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date approvedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date approvedAtEnd
    ) {
        Page<Contribution> contributionPage = contributionService.findByParams(
                page,
                limit,
                sortBy,
                sortDirection,
                status,
                type,
                user,
                submittedAtStart,
                submittedAtEnd,
                approvedAtStart,
                approvedAtEnd
        );

        PageResponse<ContributionListItemResponse> response = PageMapper.toPagedResponse(contributionPage, ContributionMapper::toContributionListItemResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<ContributionsByUserResponse>> findByParamsGroupedByUser(
            @RequestParam(defaultValue = "50", required = false) Integer limit,
            @RequestParam(defaultValue = "DESC", required = false) String sortDirection,
            @RequestParam(required = false) List<Long> status,
            @RequestParam(required = false) List<Long> type,
            @RequestParam(required = false) List<Long> user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date submittedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date submittedAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date approvedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date approvedAtEnd
    ) {
        Page<Contribution> contributions = contributionService.findByParams(
                0,
                limit,
                "approvedAt",
                sortDirection,
                status,
                type,
                user,
                submittedAtStart,
                submittedAtEnd,
                approvedAtStart,
                approvedAtEnd
        );

        List<ContributionsByUserResponse> response = new ArrayList<>(ContributionMapper.toContributionsByUserResponse(contributions.getContent()));

        Comparator<ContributionsByUserResponse> comparator = Comparator
                .comparingInt((ContributionsByUserResponse item) -> item.contributions().size());

        comparator = comparator.thenComparing(item -> item.contributions().stream()
                .map(ContributionListItemResponse::approvedAt)
                .filter(Objects::nonNull)
                .max(Date::compareTo)
                .orElse(new Date(0)));

        if ("DESC".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }

        response.sort(comparator);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution:create')")
    public ResponseEntity<ContributionResponse> save(@RequestBody @Valid ContributionRequest request) {
        Contribution newContribution = ContributionMapper.toContribution(request);
        Contribution savedContribution = contributionService.save(newContribution);
        return ResponseEntity.status(HttpStatus.CREATED).body(ContributionMapper.toContributionResponse(savedContribution));
    }


}
