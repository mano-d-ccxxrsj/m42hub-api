package com.m42hub.m42hub_api.donation.service;

import com.m42hub.m42hub_api.donation.entity.Type;
import com.m42hub.m42hub_api.donation.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("donationTypeService")
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository repository;

    @Transactional(readOnly = true)
    public List<Type> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Type> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Type save(Type type) {
        return repository.save(type);
    }
}
