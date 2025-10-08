package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.project.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;

    @Transactional
    public Role save(Role status) {
        return repository.save(status);
    }

    @Transactional(readOnly = true)
    public Optional<Role> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public Map<Long, Role> findAllByIds(List<Long> roleIds) {
        List<Role> roles = repository.findAllById(roleIds);
        return roles.stream().collect(Collectors.toMap(Role::getId, Function.identity()));
    }

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return repository.findAllByOrderByNameAsc();
    }
}