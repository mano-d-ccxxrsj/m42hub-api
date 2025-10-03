package com.m42hub.m42hub_api.donation.controller;

import com.m42hub.m42hub_api.donation.dto.request.DonationRequest;
import com.m42hub.m42hub_api.donation.dto.response.DonationListItemResponse;
import com.m42hub.m42hub_api.donation.dto.response.DonationResponse;
import com.m42hub.m42hub_api.donation.entity.Donation;
import com.m42hub.m42hub_api.donation.mapper.DonationMapper;
import com.m42hub.m42hub_api.donation.service.DonationService;
import com.m42hub.m42hub_api.shared.dto.PageResponse;
import com.m42hub.m42hub_api.shared.mapper.PageMapper;
import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
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

    private final DonationService donationService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('donation:create')")
    public ResponseEntity<List<DonationResponse>> getAll() {
        return ResponseEntity.ok(donationService.findAll()
                .stream()
                .map(DonationMapper::toDonationResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('donation:get_by_id')")
    public ResponseEntity<DonationResponse> getById(@PathVariable UUID id) {
        return donationService.findById(id)
                .map(donation -> ResponseEntity.ok(DonationMapper.toDonationResponse(donation)))
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
            @RequestParam(required = false) List<Long> user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date donatedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date donatedAtEnd,
            @RequestParam(required = false) BigDecimal minTotalAmount,
            @RequestParam(required = false) BigDecimal maxTotalAmount
    ) {
        Page<Donation> donationPage = donationService.findByParams(
                page,
                limit,
                sortBy,
                sortDirection,
                status,
                type,
                platform,
                user,
                donatedAtStart,
                donatedAtEnd,
                minTotalAmount,
                maxTotalAmount
        );

        PageResponse<DonationListItemResponse> response = PageMapper.toPagedResponse(donationPage, DonationMapper::toDonationListItemResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<UserInfoResponse>> donationRanking(
            @RequestParam(defaultValue = "50", required = false) Integer limit,
            @RequestParam(defaultValue = "DESC", required = false) String sortDirection,
            @RequestParam(required = false) List<Long> status,
            @RequestParam(required = false) List<Long> type,
            @RequestParam(required = false) List<Long> platform,
            @RequestParam(required = false) List<Long> user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date donatedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date donatedAtEnd,
            @RequestParam(required = false) BigDecimal minTotalAmount,
            @RequestParam(required = false) BigDecimal maxTotalAmount
    ) {
        List<User> rankingList = donationService.donationRanking(
                limit,
                sortDirection,
                status,
                type,
                platform,
                user,
                donatedAtStart,
                donatedAtEnd,
                minTotalAmount,
                maxTotalAmount
        );

        List<UserInfoResponse> response = rankingList.stream()
                .map(UserMapper::toUserInfoResponse)
                .toList();

        return ResponseEntity.ok(response);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('donation:create')")
    public ResponseEntity<DonationResponse> save(@RequestBody @Valid DonationRequest request) {
        Donation newDonation = DonationMapper.toDonation(request);
        Donation savedDonation = donationService.save(newDonation);
        return ResponseEntity.status(HttpStatus.CREATED).body(DonationMapper.toDonationResponse(savedDonation));
    }


}
