package com.m42hub.m42hub_api.abuse.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m42hub.m42hub_api.abuse.dto.response.AbuseCategoryResponse;
import com.m42hub.m42hub_api.abuse.mapper.AbuseCategoryMapper;
import com.m42hub.m42hub_api.abuse.service.AbuseCategoryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/abuse/category")
@AllArgsConstructor
public class AbuseCategoryController {

    private final AbuseCategoryService service;

    @GetMapping()
    public ResponseEntity<List<AbuseCategoryResponse>> getAllAbuses() {
        return ResponseEntity.ok(service.findAll().stream().map(AbuseCategoryMapper::toAbuseCategoryResponse).toList());
    }

}
