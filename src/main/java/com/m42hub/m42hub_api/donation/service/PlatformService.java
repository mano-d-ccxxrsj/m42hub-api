package com.m42hub.m42hub_api.donation.service;

import com.m42hub.m42hub_api.donation.entity.Platform;
import com.m42hub.m42hub_api.donation.repository.PlatformRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("donationPlatformService")
@RequiredArgsConstructor
public class PlatformService {

    private final PlatformRepository repository;

    @Transactional(readOnly = true)
    public List<Platform> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Platform> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Platform save(Platform platform) {
        return repository.save(platform);
    }
}
