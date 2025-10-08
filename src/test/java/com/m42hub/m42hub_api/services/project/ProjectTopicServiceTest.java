package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.ProjectTopic;
import com.m42hub.m42hub_api.project.repository.ProjectTopicRepository;
import com.m42hub.m42hub_api.project.service.ProjectTopicService;
import com.m42hub.m42hub_api.services.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ProjectTopicServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ProjectTopicServiceTest.class);

    private final int wantedNumberOfInvocations = 1;

    private static final Long PRIMARY_PROJECT_TOPIC_ID = 1L;
    private static final Long SECONDARY_PROJECT_TOPIC_ID = 1L;
    private static final Long TERTIARY_PROJECT_TOPIC_ID = 3L;

    private final UUID PRIMARY_PROJECT_ID = TestUtils.getRandomUUID();
    private final UUID SECONDARY_PROJECT_ID = TestUtils.getRandomUUID();
    private final Long PRIMARY_TOPIC_ID = 1L;
    private final Long SECONDARY_TOPIC_ID = 2L;
    private final Long TERTIARY_TOPIC_ID = 3L;

    @Mock
    private ProjectTopicRepository repository;

    @InjectMocks
    private ProjectTopicService service;

    private AutoCloseable mocks;

    private ProjectTopic projectTopicPrimary;
    private ProjectTopic projectTopicSecondary;
    private ProjectTopic projectTopicTertiary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        projectTopicPrimary = TestUtils.createProjectTopic(PRIMARY_PROJECT_TOPIC_ID, PRIMARY_PROJECT_ID, PRIMARY_TOPIC_ID);
        projectTopicSecondary = TestUtils.createProjectTopic(SECONDARY_PROJECT_TOPIC_ID, PRIMARY_PROJECT_ID, SECONDARY_TOPIC_ID);
        projectTopicTertiary = TestUtils.createProjectTopic(TERTIARY_PROJECT_TOPIC_ID, SECONDARY_PROJECT_ID, TERTIARY_TOPIC_ID);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllProjectTopics_whenFindAllIsCalled() {
        // GIVEN
        List<ProjectTopic> topics = List.of(projectTopicPrimary, projectTopicSecondary, projectTopicTertiary);
        Mockito.when(repository.findAll()).thenReturn(topics);

        // WHEN
        List<ProjectTopic> result = service.findAll();

        // THEN
        Assertions.assertThat(result)
                .hasSize(3)
                .containsExactlyInAnyOrder(projectTopicPrimary, projectTopicSecondary, projectTopicTertiary);

        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findAll();
    }

    @Test
    public void shouldReturnProjectTopic_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(repository.findById(PRIMARY_PROJECT_TOPIC_ID))
                .thenReturn(Optional.of(projectTopicPrimary));

        // WHEN
        Optional<ProjectTopic> result = service.findById(PRIMARY_PROJECT_TOPIC_ID);

        // THEN
        Assertions.assertThat(result)
                .isPresent()
                .containsSame(projectTopicPrimary);

        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findById(PRIMARY_PROJECT_TOPIC_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 4L;
        Mockito.when(repository.findById(invalidId)).thenReturn(Optional.empty());

        // WHEN
        Optional<ProjectTopic> result = service.findById(invalidId);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findById(invalidId);
    }

    @Test
    public void shouldReturnProjectTopicsByProjectId_whenFindByProjectIdIsCalled() {
        // GIVEN
        List<ProjectTopic> topics = List.of(projectTopicPrimary, projectTopicSecondary);
        Mockito.when(repository.findByProjectId(PRIMARY_PROJECT_ID)).thenReturn(topics);

        // WHEN
        List<ProjectTopic> result = service.findByProjectId(PRIMARY_PROJECT_ID);

        // THEN
        Assertions.assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(projectTopicPrimary, projectTopicSecondary)
                .allMatch(pt -> pt.getProjectId().equals(PRIMARY_PROJECT_ID));

        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByProjectId(PRIMARY_PROJECT_ID);
    }

    @Test
    public void shouldReturnEmptyList_whenFindByProjectIdWithNoTopics() {
        // GIVEN
        UUID projectWithNoTopics = TestUtils.getRandomUUID();
        Mockito.when(repository.findByProjectId(projectWithNoTopics))
                .thenReturn(Collections.emptyList());

        // WHEN
        List<ProjectTopic> result = service.findByProjectId(projectWithNoTopics);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByProjectId(projectWithNoTopics);
    }

    @Test
    public void shouldReturnProjectIdsByTopicIds_whenGetProjectIdsByTopicIdsIsCalled() {
        // GIVEN
        List<Long> topicIds = List.of(PRIMARY_TOPIC_ID, SECONDARY_TOPIC_ID);
        List<ProjectTopic> topics = List.of(projectTopicPrimary, projectTopicSecondary);
        Mockito.when(repository.findByTopicIdIn(topicIds)).thenReturn(topics);

        // WHEN
        Set<UUID> result = service.getProjectIdsByTopicIds(topicIds);

        // THEN
        Assertions.assertThat(result).hasSize(1).containsExactly(PRIMARY_PROJECT_ID);
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByTopicIdIn(topicIds);
    }

    @Test
    public void shouldReturnMultipleProjectIdsByTopicIds_whenGetProjectIdsByTopicIdsIsCalled() {
        // GIVEN
        List<Long> topicIds = List.of(PRIMARY_TOPIC_ID, TERTIARY_TOPIC_ID);
        List<ProjectTopic> topics = List.of(projectTopicPrimary, projectTopicTertiary);
        Mockito.when(repository.findByTopicIdIn(topicIds)).thenReturn(topics);

        // WHEN
        Set<UUID> result = service.getProjectIdsByTopicIds(topicIds);

        // THEN
        Assertions.assertThat(result).hasSize(2).containsExactlyInAnyOrder(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID);
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByTopicIdIn(topicIds);
    }

    @Test
    public void shouldReturnEmptySet_whenGetProjectIdsByTopicIdsWithEmptyList() {
        // GIVEN
        List<Long> emptyTopicIds = Collections.emptyList();

        // WHEN
        Set<UUID> result = service.getProjectIdsByTopicIds(emptyTopicIds);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.never()).findByTopicIdIn(ArgumentMatchers.any());
    }

    @Test
    public void shouldReturnEmptySet_whenGetProjectIdsByTopicIdsWithNull() {
        // GIVEN
        List<Long> nullTopicIds = null;

        // WHEN
        Set<UUID> result = service.getProjectIdsByTopicIds(nullTopicIds);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.never()).findByTopicIdIn(ArgumentMatchers.any());
    }

    @Test
    public void shouldReturnEmptySet_whenGetProjectIdsByTopicIdsWithNonExistingTopics() {
        // GIVEN
        List<Long> nonExistingTopicIds = List.of(4L, 5L);
        Mockito.when(repository.findByTopicIdIn(nonExistingTopicIds)).thenReturn(Collections.emptyList());

        // WHEN
        Set<UUID> result = service.getProjectIdsByTopicIds(nonExistingTopicIds);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByTopicIdIn(nonExistingTopicIds);
    }
}