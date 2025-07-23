package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Topic;
import com.m42hub.m42hub_api.project.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository repository;

    public List<Topic> findAll() {
        return repository.findAll();
    }

    public Optional<Topic> findById(Long id) {
        return repository.findById(id);
    }

    public Topic save(Topic topic) {
        return repository.save(topic);
    }

    public Optional<Topic> changeColor(Long topicId, String hexColor) {
        Optional<Topic> optTopic = repository.findById(topicId);
        if (optTopic.isPresent()) {
            Topic topic = optTopic.get();
            topic.setHexColor(hexColor);

            repository.save(topic);
            return Optional.of(topic);
        }

        return Optional.empty();
    }

}
