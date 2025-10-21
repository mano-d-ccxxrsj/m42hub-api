package com.m42hub.m42hub_api.abuse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.m42hub.m42hub_api.abuse.entity.AbuseStatus;
import com.m42hub.m42hub_api.abuse.repository.AbuseStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AbuseStatusService {

    private final AbuseStatusRepository abuseStatusRepository;

    public List<AbuseStatus> findAll() {
        return abuseStatusRepository.findAll();
    }

    public AbuseStatus findByName(String name) {
        return abuseStatusRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Status não encontrado: " + name));
    }

    public AbuseStatus findById(Long id) {
        return abuseStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Status não encontrado: " + id));
    }
}