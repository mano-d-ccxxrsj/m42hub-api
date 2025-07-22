package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Status;
import com.m42hub.m42hub_api.project.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository repository;

    public List<Status> findAll() { return repository.findAll(); }

    public Status save(Status status) { return  repository.save(status); }
}
