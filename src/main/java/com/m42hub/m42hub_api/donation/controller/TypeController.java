package com.m42hub.m42hub_api.donation.controller;

import com.m42hub.m42hub_api.donation.dto.request.TypeRequest;
import com.m42hub.m42hub_api.donation.dto.response.TypeResponse;
import com.m42hub.m42hub_api.donation.entity.Type;
import com.m42hub.m42hub_api.donation.mapper.TypeMapper;
import com.m42hub.m42hub_api.donation.service.TypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("donationTypeController")
@RequestMapping("/api/v1/donation/type")
@RequiredArgsConstructor
public class TypeController {

    private final TypeService typeService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('donation_type:create')")
    public ResponseEntity<List<TypeResponse>> getAll() {
        return ResponseEntity.ok(typeService.findAll()
                .stream()
                .map(TypeMapper::toTypeResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('donation_type:get_by_id')")
    public ResponseEntity<TypeResponse> getById(@PathVariable Long id) {
        return typeService.findById(id)
                .map(type -> ResponseEntity.ok(TypeMapper.toTypeResponse(type)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('donation_type:create')")
    public ResponseEntity<TypeResponse> save(@RequestBody @Valid TypeRequest request) {
        Type newType = TypeMapper.toType(request);
        Type savedType = typeService.save(newType);
        return ResponseEntity.status(HttpStatus.CREATED).body(TypeMapper.toTypeResponse(savedType));
    }


}
