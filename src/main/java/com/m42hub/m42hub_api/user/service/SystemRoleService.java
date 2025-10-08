package com.m42hub.m42hub_api.user.service;

import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.repository.SystemRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemRoleService {

    private final SystemRoleRepository repository;

    @Transactional
    public SystemRole save(SystemRole systemRole) {
        return repository.save(systemRole);
    }

    @Transactional(readOnly = true)
    public Optional<SystemRole> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<SystemRole> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Map<UUID, SystemRole> findAllByIds(List<UUID> ids) {
        List<SystemRole> systemRoles = repository.findAllById(ids);
        return systemRoles.stream().collect(Collectors.toMap(SystemRole::getId, Function.identity()));
    }
}