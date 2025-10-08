package com.m42hub.m42hub_api.donation.controller;

import com.m42hub.m42hub_api.donation.dto.request.DonationRequest;
import com.m42hub.m42hub_api.donation.dto.response.*;
import com.m42hub.m42hub_api.donation.entity.Donation;
import com.m42hub.m42hub_api.donation.entity.Platform;
import com.m42hub.m42hub_api.donation.entity.Status;
import com.m42hub.m42hub_api.donation.entity.Type;
import com.m42hub.m42hub_api.donation.mapper.DonationMapper;
import com.m42hub.m42hub_api.donation.mapper.PlatformMapper;
import com.m42hub.m42hub_api.donation.mapper.StatusMapper;
import com.m42hub.m42hub_api.donation.mapper.TypeMapper;
import com.m42hub.m42hub_api.donation.service.DonationService;
import com.m42hub.m42hub_api.donation.service.PlatformService;
import com.m42hub.m42hub_api.donation.service.StatusService;
import com.m42hub.m42hub_api.donation.service.TypeService;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/donation")
@RequiredArgsConstructor
public class DonationController {

    private final UserInterestProjectRoleService userInterestProjectRoleService;
    private final SystemRoleService systemRoleService;
    private final DonationService donationService;
    private final PlatformService platformService;
    private final StatusService statusService;
    private final TypeService typeService;
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('donation:create')")
    public ResponseEntity<List<DonationResponse>> getAll() {
        List<Donation> donations = donationService.findAll();
        List<DonationResponse> response = donations.stream()
                .map(this::buildDonationResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('donation:get_by_id')")
    public ResponseEntity<DonationResponse> getById(@PathVariable UUID id) {
        return donationService.findById(id)
                .map(this::buildDonationResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('donation:search')")
    public ResponseEntity<PageResponse<DonationListItemResponse>> findByParams(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "50", required = false) Integer limit,
            @RequestParam(defaultValue = "amount", required = false) String sortBy,
            @RequestParam(defaultValue = "DESC", required = false) String sortDirection,
            @RequestParam(required = false) List<Long> status,
            @RequestParam(required = false) List<Long> type,
            @RequestParam(required = false) List<Long> platform,
            @RequestParam(required = false) List<UUID> user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date donatedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date donatedAtEnd,
            @RequestParam(required = false) BigDecimal minTotalAmount,
            @RequestParam(required = false) BigDecimal maxTotalAmount
    ) {
        Page<Donation> donationPage = donationService.findByParams(
                page, limit, sortBy, sortDirection, status, type, platform, user,
                donatedAtStart, donatedAtEnd, minTotalAmount, maxTotalAmount
        );

        PageResponse<DonationListItemResponse> response = PageMapper.toPagedResponse(
                donationPage,
                this::buildDonationListItemResponse
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<UserInfoResponse>> donationRanking(
            @RequestParam(defaultValue = "50", required = false) Integer limit,
            @RequestParam(defaultValue = "DESC", required = false) String sortDirection,
            @RequestParam(required = false) List<Long> status,
            @RequestParam(required = false) List<Long> type,
            @RequestParam(required = false) List<Long> platform,
            @RequestParam(required = false) List<UUID> user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date donatedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date donatedAtEnd,
            @RequestParam(required = false) BigDecimal minTotalAmount,
            @RequestParam(required = false) BigDecimal maxTotalAmount
    ) {
        List<User> rankingList = donationService.donationRanking(
                limit, sortDirection, status, type, platform, user,
                donatedAtStart, donatedAtEnd, minTotalAmount, maxTotalAmount
        );

        List<UserInfoResponse> response = rankingList.stream()
                .map(this::buildUserInfoResponse)
                .toList();

        return ResponseEntity.ok(response);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('donation:create')")
    public ResponseEntity<DonationResponse> save(@RequestBody @Valid DonationRequest request) {
        Donation newDonation = DonationMapper.toDonation(request);
        Donation savedDonation = donationService.save(newDonation);
        DonationResponse response = buildDonationResponse(savedDonation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private DonationResponse buildDonationResponse(Donation donation) {
        StatusResponse status = statusService.findById(donation.getStatusId())
                .map(StatusMapper::toStatusResponse)
                .orElse(null);
        TypeResponse type = typeService.findById(donation.getTypeId())
                .map(TypeMapper::toTypeResponse)
                .orElse(null);
        PlatformResponse platform = platformService.findById(donation.getPlatformId())
                .map(PlatformMapper::toPlatformResponse)
                .orElse(null);
        UserInfoResponse userInfo = userService.findById(donation.getUserId())
                .map(this::buildUserInfoResponse)
                .orElse(null);

        return DonationMapper.toDonationResponse(donation, status, type, platform, userInfo);
    }

    private DonationListItemResponse buildDonationListItemResponse(Donation donation) {
        String statusName = statusService.findById(donation.getStatusId())
                .map(Status::getName)
                .orElse(null);
        String typeName = typeService.findById(donation.getTypeId())
                .map(Type::getName)
                .orElse(null);
        String platformName = platformService.findById(donation.getPlatformId())
                .map(Platform::getName)
                .orElse(null);
        UserInfoResponse userInfo = userService.findById(donation.getUserId())
                .map(this::buildUserInfoResponse)
                .orElse(null);

        return DonationMapper.toDonationListItemResponse(donation, statusName, typeName, platformName, userInfo);
    }

    private UserInfoResponse buildUserInfoResponse(User user) {
        SystemRole systemRole = systemRoleService.findById(user.getSystemRoleId()).orElse(null);
        List<Role> interestRoles = userInterestProjectRoleService.getRolesByUser(user.getId());
        return UserMapper.toUserInfoResponse(user, systemRole, interestRoles);
    }
}