package com.m42hub.m42hub_api.abuse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m42hub.m42hub_api.abuse.dto.request.AbuseCategoryRequest;
import com.m42hub.m42hub_api.abuse.dto.response.AbuseCategoryResponse;
import com.m42hub.m42hub_api.abuse.mapper.AbuseCategoryMapper;
import com.m42hub.m42hub_api.abuse.service.AbuseCategoryService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/abuse/category")
@AllArgsConstructor
public class AbuseCategoryController {

    private final AbuseCategoryService service;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('abuse-category:update')")
    public ResponseEntity<List<AbuseCategoryResponse>> getAllAbuses() {
        return ResponseEntity.ok(service.findAll().stream().map(AbuseCategoryMapper::toAbuseCategoryResponse).toList());
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('abuse-category:create')")
    public ResponseEntity<AbuseCategoryResponse> createCategory(@Valid @RequestBody AbuseCategoryRequest request) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AbuseCategoryMapper.toAbuseCategoryResponse(
                            service.create(AbuseCategoryMapper.toAbuseCategory(request))));
       
    }

    
}
