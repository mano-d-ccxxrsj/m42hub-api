package com.m42hub.m42hub_api.user.service;

import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.repository.SystemRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SystemRoleService {

    private final SystemRoleRepository repository;
    private final PermissionService permissionService;

    @Transactional(readOnly = true)
    public List<SystemRole> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<SystemRole> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public SystemRole save(SystemRole systemRole) {
        systemRole.setPermissions(this.findPermissions(systemRole.getPermissions()));
        return repository.save(systemRole);
    }

    @Transactional
    private List<Permission> findPermissions(List<Permission> permissions) {
        List<Permission> permissionsFound = new ArrayList<>();
        permissions.forEach(permission -> permissionService.findById(permission.getId()).ifPresent(permissionsFound::add));
        return permissionsFound;
    }

}
