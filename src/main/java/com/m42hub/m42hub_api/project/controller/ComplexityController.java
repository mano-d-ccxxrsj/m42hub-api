package com.m42hub.m42hub_api.project.controller;

import com.m42hub.m42hub_api.project.dto.request.ComplexityRequest;
import com.m42hub.m42hub_api.project.dto.request.StatusRequest;
import com.m42hub.m42hub_api.project.dto.response.ComplexityResponse;
import com.m42hub.m42hub_api.project.dto.response.StatusResponse;
import com.m42hub.m42hub_api.project.entity.Complexity;
import com.m42hub.m42hub_api.project.entity.Status;
import com.m42hub.m42hub_api.project.mapper.ComplexityMapper;
import com.m42hub.m42hub_api.project.mapper.StatusMapper;
import com.m42hub.m42hub_api.project.service.ComplexityService;
import com.m42hub.m42hub_api.project.service.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project/complexity")
@RequiredArgsConstructor
public class ComplexityController {

    private final ComplexityService complexityService;

    @GetMapping()
    public ResponseEntity<List<ComplexityResponse>> getAll() {
        return ResponseEntity.ok(complexityService.findAll()
                .stream()
                .map(ComplexityMapper::toComplexityResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplexityResponse> getById(@PathVariable Long id) {
        return complexityService.findById(id)
                .map(complexity -> ResponseEntity.ok(ComplexityMapper.toComplexityResponse(complexity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ComplexityResponse>  save(@RequestBody ComplexityRequest request) {
        Complexity newComplexity = ComplexityMapper.toComplexity(request);
        Complexity savedComplexity = complexityService.save(newComplexity);
        return ResponseEntity.status(HttpStatus.CREATED).body(ComplexityMapper.toComplexityResponse(savedComplexity));
    }



}
