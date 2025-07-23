package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.project.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;

    public List<Role> findAll() {
        return repository.findAll();
    }

    public Optional<Role> findById(Long id) {
        return repository.findById(id);
    }

    public Role save(Role status) {
        return repository.save(status);
    }

}
