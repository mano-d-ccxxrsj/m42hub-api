package com.m42hub.m42hub_api.user.service;

import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository repository;

    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Permission> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Permission save(Permission permission) {
        return repository.save(permission);
    }

    @Transactional
    public void registerPermission(String name) {
        if (!repository.existsByName(name)) {
            Permission permission = Permission
                    .builder()
                    .name(name)
                    .build();
            repository.save(permission);
        }
    }
}
