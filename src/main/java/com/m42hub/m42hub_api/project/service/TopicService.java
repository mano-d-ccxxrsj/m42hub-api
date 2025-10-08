package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.ProjectTopic;
import com.m42hub.m42hub_api.project.entity.Topic;
import com.m42hub.m42hub_api.project.repository.ProjectTopicRepository;
import com.m42hub.m42hub_api.project.repository.TopicRepository;
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
public class TopicService {

    private final ProjectTopicRepository projectTopicRepository;
    private final TopicRepository repository;

    @Transactional
    public Topic save(Topic topic) {
        return repository.save(topic);
    }

    @Transactional(readOnly = true)
    public Optional<Topic> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Topic> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    @Transactional
    public Optional<Topic> changeColor(Long topicId, String hexColor) {
        return repository.findById(topicId).map(topic -> {
            Optional.ofNullable(hexColor).ifPresent(topic::setHexColor);
            return repository.save(topic);
        });
    }

    @Transactional(readOnly = true)
    public List<Topic> findTopicsByProjectId(UUID projectId) {
        List<ProjectTopic> projectTopics = projectTopicRepository.findByProjectId(projectId);
        List<Long> topicIds = projectTopics.stream()
                .map(ProjectTopic::getTopicId)
                .toList();
        return repository.findAllById(topicIds);
    }

    @Transactional(readOnly = true)
    public Map<UUID, List<Topic>> findTopicsByProjectIds(List<UUID> projectIds) {
        List<ProjectTopic> projectTopics = projectTopicRepository.findByProjectIdIn(projectIds);
        List<Long> topicIds = projectTopics.stream().map(ProjectTopic::getTopicId).distinct().toList();
        List<Topic> topics = repository.findAllById(topicIds);
        Map<Long, Topic> topicMap = topics.stream().collect(Collectors.toMap(Topic::getId, Function.identity()));

        return projectTopics.stream()
                .collect(Collectors.groupingBy(ProjectTopic::getProjectId,
                        Collectors.mapping(pt -> topicMap.get(pt.getTopicId()), Collectors.toList())));
    }
}