package com.m42hub.m42hub_api.abuse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;
import com.m42hub.m42hub_api.abuse.repository.AbuseCategoryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AbuseCategoryService {

    private final AbuseCategoryRepository repository;

    public List<AbuseCategory> findAll() {
        return repository.findAll();
    }

}
