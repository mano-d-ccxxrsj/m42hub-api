package com.m42hub.m42hub_api.donation.service;

import com.m42hub.m42hub_api.donation.entity.Donation;
import com.m42hub.m42hub_api.donation.entity.Platform;
import com.m42hub.m42hub_api.donation.entity.Status;
import com.m42hub.m42hub_api.donation.entity.Type;
import com.m42hub.m42hub_api.donation.repository.DonationRepository;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository repository;
    private final UserService userService;
    private final StatusService statusService;
    private final TypeService typeService;
    private final PlatformService platformService;


    @Transactional(readOnly = true)
    public List<Donation> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Donation> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public Donation save(Donation donation) {
        donation.setUser(findUser(donation.getUser()));
        donation.setStatus(findStatus(donation.getStatus()));
        donation.setType(findType(donation.getType()));
        donation.setPlatform(findPlatform(donation.getPlatform()));

        return repository.save(donation);
    }

    private User findUser(User user) {
        return userService.findById(user.getId()).orElse(null);
    }

    private Status findStatus(Status status) {
        return statusService.findById(status.getId()).orElse(null);
    }

    private Type findType(Type type) {
        return typeService.findById(type.getId()).orElse(null);
    }

    private Platform findPlatform(Platform platform) {
        return platformService.findById(platform.getId()).orElse(null);
    }


}
