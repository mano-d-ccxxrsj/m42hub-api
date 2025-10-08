package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.ProjectTool;
import com.m42hub.m42hub_api.project.repository.ProjectToolRepository;
import com.m42hub.m42hub_api.project.service.ProjectToolService;
import com.m42hub.m42hub_api.services.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ProjectToolServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ProjectToolServiceTest.class);

    private final int wantedNumberOfInvocations = 1;

    private static final Long PRIMARY_PROJECT_TOOL_ID = 1L;
    private static final Long SECONDARY_PROJECT_TOOL_ID = 2L;
    private static final Long TERTIARY_PROJECT_TOOL_ID = 3L;

    private final UUID PRIMARY_PROJECT_ID = TestUtils.getRandomUUID();
    private final UUID SECONDARY_PROJECT_ID = TestUtils.getRandomUUID();
    private static final Long PRIMARY_TOOL_ID = 1L;
    private static final Long SECONDARY_TOOL_ID = 2L;
    private static final Long TERTIARY_TOOL_ID = 3L;

    @Mock
    private ProjectToolRepository repository;

    @InjectMocks
    private ProjectToolService service;

    private AutoCloseable mocks;

    private ProjectTool projectToolPrimary;
    private ProjectTool projectToolSecondary;
    private ProjectTool projectToolTertiary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        projectToolPrimary = TestUtils.createProjectTool(PRIMARY_PROJECT_TOOL_ID, PRIMARY_PROJECT_ID, PRIMARY_TOOL_ID);
        projectToolSecondary = TestUtils.createProjectTool(SECONDARY_PROJECT_TOOL_ID, PRIMARY_PROJECT_ID, SECONDARY_TOOL_ID);
        projectToolTertiary = TestUtils.createProjectTool(TERTIARY_PROJECT_TOOL_ID, SECONDARY_PROJECT_ID, TERTIARY_TOOL_ID);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllProjectTools_whenFindAllIsCalled() {
        // GIVEN
        List<ProjectTool> tools = List.of(projectToolPrimary, projectToolSecondary, projectToolTertiary);
        Mockito.when(repository.findAll()).thenReturn(tools);

        // WHEN
        List<ProjectTool> result = service.findAll();

        // THEN
        Assertions.assertThat(result)
                .hasSize(3)
                .containsExactlyInAnyOrder(projectToolPrimary, projectToolSecondary, projectToolTertiary);

        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findAll();
    }

    @Test
    public void shouldReturnProjectTool_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(repository.findById(PRIMARY_PROJECT_TOOL_ID))
                .thenReturn(Optional.of(projectToolPrimary));

        // WHEN
        Optional<ProjectTool> result = service.findById(PRIMARY_PROJECT_TOOL_ID);

        // THEN
        Assertions.assertThat(result)
                .isPresent()
                .containsSame(projectToolPrimary);

        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findById(PRIMARY_PROJECT_TOOL_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 4L;
        Mockito.when(repository.findById(invalidId)).thenReturn(Optional.empty());

        // WHEN
        Optional<ProjectTool> result = service.findById(invalidId);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findById(invalidId);
    }

    @Test
    public void shouldReturnProjectToolsByProjectId_whenFindByProjectIdIsCalled() {
        // GIVEN
        List<ProjectTool> tools = List.of(projectToolPrimary, projectToolSecondary);
        Mockito.when(repository.findByProjectId(PRIMARY_PROJECT_ID)).thenReturn(tools);

        // WHEN
        List<ProjectTool> result = service.findByProjectId(PRIMARY_PROJECT_ID);

        // THEN
        Assertions.assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(projectToolPrimary, projectToolSecondary)
                .allMatch(pt -> pt.getProjectId().equals(PRIMARY_PROJECT_ID));

        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByProjectId(PRIMARY_PROJECT_ID);
    }

    @Test
    public void shouldReturnEmptyList_whenFindByProjectIdWithNoTools() {
        // GIVEN
        UUID projectWithNoTools = TestUtils.getRandomUUID();
        Mockito.when(repository.findByProjectId(projectWithNoTools)).thenReturn(Collections.emptyList());

        // WHEN
        List<ProjectTool> result = service.findByProjectId(projectWithNoTools);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByProjectId(projectWithNoTools);
    }

    @Test
    public void shouldReturnProjectIdsByToolIds_whenGetProjectIdsByToolIdsIsCalled() {
        // GIVEN
        List<Long> toolIds = List.of(PRIMARY_TOOL_ID, SECONDARY_TOOL_ID);
        List<ProjectTool> tools = List.of(projectToolPrimary, projectToolSecondary);
        Mockito.when(repository.findByToolIdIn(toolIds)).thenReturn(tools);

        // WHEN
        Set<UUID> result = service.getProjectIdsByToolIds(toolIds);

        // THEN
        Assertions.assertThat(result).hasSize(1).containsExactly(PRIMARY_PROJECT_ID);
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByToolIdIn(toolIds);
    }

    @Test
    public void shouldReturnMultipleProjectIdsByToolIds_whenGetProjectIdsByToolIdsIsCalled() {
        // GIVEN
        List<Long> toolIds = List.of(PRIMARY_TOOL_ID, TERTIARY_TOOL_ID);
        List<ProjectTool> tools = List.of(projectToolPrimary, projectToolTertiary);
        Mockito.when(repository.findByToolIdIn(toolIds)).thenReturn(tools);

        // WHEN
        Set<UUID> result = service.getProjectIdsByToolIds(toolIds);

        // THEN
        Assertions.assertThat(result).hasSize(2).containsExactlyInAnyOrder(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID);
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByToolIdIn(toolIds);
    }

    @Test
    public void shouldReturnEmptySet_whenGetProjectIdsByToolIdsWithEmptyList() {
        // GIVEN
        List<Long> emptyToolIds = Collections.emptyList();

        // WHEN
        Set<UUID> result = service.getProjectIdsByToolIds(emptyToolIds);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.never()).findByToolIdIn(ArgumentMatchers.any());
    }

    @Test
    public void shouldReturnEmptySet_whenGetProjectIdsByToolIdsWithNull() {
        // GIVEN
        List<Long> nullToolIds = null;

        // WHEN
        Set<UUID> result = service.getProjectIdsByToolIds(nullToolIds);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.never()).findByToolIdIn(ArgumentMatchers.any());
    }

    @Test
    public void shouldReturnEmptySet_whenGetProjectIdsByToolIdsWithNonExistingTools() {
        // GIVEN
        List<Long> nonExistingToolIds = List.of(4L, 5L);
        Mockito.when(repository.findByToolIdIn(nonExistingToolIds)).thenReturn(Collections.emptyList());

        // WHEN
        Set<UUID> result = service.getProjectIdsByToolIds(nonExistingToolIds);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByToolIdIn(nonExistingToolIds);
    }
}