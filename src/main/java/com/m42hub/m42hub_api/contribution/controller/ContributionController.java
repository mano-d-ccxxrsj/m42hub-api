package com.m42hub.m42hub_api.contribution.controller;

import com.m42hub.m42hub_api.contribution.dto.request.ContributionRequest;
import com.m42hub.m42hub_api.contribution.dto.response.*;
import com.m42hub.m42hub_api.contribution.entity.Contribution;
import com.m42hub.m42hub_api.contribution.entity.Status;
import com.m42hub.m42hub_api.contribution.entity.Type;
import com.m42hub.m42hub_api.contribution.enums.ContributionSortField;
import com.m42hub.m42hub_api.contribution.mapper.ContributionMapper;
import com.m42hub.m42hub_api.contribution.mapper.StatusMapper;
import com.m42hub.m42hub_api.contribution.mapper.TypeMapper;
import com.m42hub.m42hub_api.contribution.service.ContributionService;
import com.m42hub.m42hub_api.contribution.service.StatusService;
import com.m42hub.m42hub_api.contribution.service.TypeService;
import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.shared.dto.PageResponse;
import com.m42hub.m42hub_api.shared.mapper.PageMapper;
import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
import com.m42hub.m42hub_api.user.service.SystemRoleService;
import com.m42hub.m42hub_api.user.service.UserInterestProjectRoleService;
import com.m42hub.m42hub_api.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/contribution")
@RequiredArgsConstructor
public class ContributionController {

    private final UserInterestProjectRoleService userInterestProjectRoleService;
    private final ContributionService contributionService;
    private final SystemRoleService systemRoleService;
    private final StatusService statusService;
    private final TypeService typeService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution:create')")
    public ResponseEntity<List<ContributionResponse>> getAll() {
        List<Contribution> contributions = contributionService.findAll();
        List<ContributionResponse> response = contributions.stream()
                .map(this::buildContributionResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution:get_by_id')")
    public ResponseEntity<ContributionResponse> getById(@PathVariable UUID id) {
        return contributionService.findById(id)
                .map(this::buildContributionResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution:search')")
    public ResponseEntity<PageResponse<ContributionListItemResponse>> findByParams(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "50", required = false) Integer limit,
            @RequestParam(defaultValue = "approvedAt", required = false) String sortBy,
            @RequestParam(defaultValue = "ASC", required = false) String sortDirection,
            @RequestParam(required = false) List<UUID> status,
            @RequestParam(required = false) List<UUID> type,
            @RequestParam(required = false) List<UUID> user,
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

        PageResponse<ContributionListItemResponse> response = PageMapper.toPagedResponse(
                contributionPage,
                this::buildContributionListItemResponse
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<ContributionsByUserResponse>> findByParamsGroupedByUser(
            @RequestParam(defaultValue = "50", required = false) Integer limit,
            @RequestParam(defaultValue = "DESC", required = false) String sortDirection,
            @RequestParam(required = false) List<UUID> status,
            @RequestParam(required = false) List<UUID> type,
            @RequestParam(required = false) List<UUID> user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date submittedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date submittedAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date approvedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date approvedAtEnd
    ) {
        Page<Contribution> contributionPage = contributionService.findByParams(
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

        List<ContributionsByUserResponse> response = buildContributionsByUserResponse(
                contributionPage.getContent(), sortDirection
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('contribution:create')")
    public ResponseEntity<ContributionResponse> save(@RequestBody @Valid ContributionRequest request) {
        Contribution newContribution = ContributionMapper.toContribution(request);
        Contribution savedContribution = contributionService.save(newContribution);
        ContributionResponse response = buildContributionResponse(savedContribution);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private ContributionResponse buildContributionResponse(Contribution contribution) {
        StatusResponse status = statusService.findById(contribution.getStatusId())
                .map(StatusMapper::toStatusResponse)
                .orElse(null);
        TypeResponse type = typeService.findById(contribution.getTypeId())
                .map(TypeMapper::toTypeResponse)
                .orElse(null);
        UserInfoResponse userInfo = userService.findById(contribution.getUserId())
                .map(this::buildUserInfoResponse)
                .orElse(null);

        return ContributionMapper.toContributionResponse(contribution, status, type, userInfo);
    }

    private ContributionListItemResponse buildContributionListItemResponse(Contribution contribution) {
        String statusName = statusService.findById(contribution.getStatusId())
                .map(Status::getName)
                .orElse(null);
        String typeName = typeService.findById(contribution.getTypeId())
                .map(Type::getName)
                .orElse(null);
        UserInfoResponse userInfo = userService.findById(contribution.getUserId())
                .map(this::buildUserInfoResponse)
                .orElse(null);

        return ContributionMapper.toContributionListItemResponse(contribution, statusName, typeName, userInfo);
    }

    private UserInfoResponse buildUserInfoResponse(User user) {
        SystemRole systemRole = systemRoleService.findById(user.getSystemRoleId()).orElse(null);
        List<Role> interestRoles = userInterestProjectRoleService.getRolesByUser(user.getId());
        return UserMapper.toUserInfoResponse(user, systemRole, interestRoles);
    }

    private List<ContributionsByUserResponse> buildContributionsByUserResponse(
            List<Contribution> contributions, String sortDirection) {

        Map<UUID, List<Contribution>> contributionsByUser = contributions.stream()
                .collect(Collectors.groupingBy(Contribution::getUserId));

        List<ContributionsByUserResponse> response = new ArrayList<>();

        for (Map.Entry<UUID, List<Contribution>> entry : contributionsByUser.entrySet()) {
            UUID userId = entry.getKey();
            List<Contribution> userContributions = entry.getValue();

            UserInfoResponse userInfo = userService.findById(userId)
                    .map(this::buildUserInfoResponse)
                    .orElse(null);

            List<ContributionListItemResponse> contributionItems = userContributions.stream()
                    .map(this::buildContributionListItemResponse)
                    .toList();

            ContributionsByUserResponse userResponse = ContributionMapper.toContributionsByUserResponse(
                    userInfo, contributionItems
            );
            response.add(userResponse);
        }

        Comparator<ContributionsByUserResponse> comparator = Comparator.comparingInt(
                (ContributionsByUserResponse item) -> item.contributions().size()
        );
        comparator = comparator.thenComparing(item -> item.contributions().stream()
                .map(ContributionListItemResponse::approvedAt)
                .filter(Objects::nonNull)
                .max(Date::compareTo)
                .orElse(new Date(0)));

        if (ContributionSortField.DESC.getFieldName().equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }

        response.sort(comparator);
        return response;
    }
}