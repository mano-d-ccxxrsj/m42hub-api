package com.m42hub.m42hub_api.donation.controller;

import com.m42hub.m42hub_api.donation.dto.request.DonationRequest;
import com.m42hub.m42hub_api.donation.dto.response.DonationResponse;
import com.m42hub.m42hub_api.donation.entity.Donation;
import com.m42hub.m42hub_api.donation.mapper.DonationMapper;
import com.m42hub.m42hub_api.donation.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('donation:create')")
    public ResponseEntity<DonationResponse> save(@RequestBody @Valid DonationRequest request) {
        Donation newDonation = DonationMapper.toDonation(request);
        Donation savedDonation = donationService.save(newDonation);
        return ResponseEntity.status(HttpStatus.CREATED).body(DonationMapper.toDonationResponse(savedDonation));
    }


}
