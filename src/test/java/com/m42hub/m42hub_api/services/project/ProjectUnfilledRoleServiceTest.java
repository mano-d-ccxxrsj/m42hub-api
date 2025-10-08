package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.project.entity.ProjectUnfilledRole;
import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.project.repository.ProjectUnfilledRoleRepository;
import com.m42hub.m42hub_api.project.repository.RoleRepository;
import com.m42hub.m42hub_api.project.service.ProjectUnfilledRoleService;
import com.m42hub.m42hub_api.services.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ProjectUnfilledRoleServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ProjectUnfilledRoleServiceTest.class);

    private int wantedNumberOfInvocations = 1;

    private static final Long PRIMARY_UNFILLED_ROLE_ID = 1L;
    private static final Long SECONDARY_UNFILLED_ROLE_ID = 2L;
    private static final Long PRIMARY_ROLE_ID = 1L;
    private static final Long SECONDARY_ROLE_ID = 2L;

    private final UUID PRIMARY_PROJECT_ID = TestUtils.getRandomUUID();
    private final UUID SECONDARY_PROJECT_ID = TestUtils.getRandomUUID();

    @Mock
    private ProjectUnfilledRoleRepository repository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private ProjectUnfilledRoleService service;

    private AutoCloseable mocks;

    private ProjectUnfilledRole unfilledRolePrimary;
    private ProjectUnfilledRole unfilledRoleSecondary;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        unfilledRolePrimary = ProjectUnfilledRole.builder()
                .id(PRIMARY_UNFILLED_ROLE_ID)
                .projectId(PRIMARY_PROJECT_ID)
                .roleId(PRIMARY_ROLE_ID)
                .build();

        unfilledRoleSecondary = ProjectUnfilledRole.builder()
                .id(SECONDARY_UNFILLED_ROLE_ID)
                .projectId(SECONDARY_PROJECT_ID)
                .roleId(SECONDARY_ROLE_ID)
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllUnfilledRoles_whenFindAllIsCalled() {
        // GIVEN
        List<ProjectUnfilledRole> roles = List.of(unfilledRolePrimary, unfilledRoleSecondary);
        Mockito.when(repository.findAll()).thenReturn(roles);

        // WHEN
        List<ProjectUnfilledRole> result = service.findAll();

        // THEN
        Assertions.assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(unfilledRolePrimary, unfilledRoleSecondary);

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnUnfilledRole_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(repository.findById(PRIMARY_UNFILLED_ROLE_ID))
                .thenReturn(Optional.of(unfilledRolePrimary));

        // WHEN
        Optional<ProjectUnfilledRole> result = service.findById(PRIMARY_UNFILLED_ROLE_ID);

        // THEN
        Assertions.assertThat(result)
                .isPresent()
                .containsSame(unfilledRolePrimary);

        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findById(PRIMARY_UNFILLED_ROLE_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        Long invalidId = 3L;
        Mockito.when(repository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<ProjectUnfilledRole> result = service.findById(invalidId);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findById(invalidId);
    }

    @Test
    public void shouldUpdateUnfilledRolesForProjectSucceed() {
        // GIVEN
        List<Long> roleIds = List.of(PRIMARY_ROLE_ID, SECONDARY_ROLE_ID);

        // WHEN
        service.updateUnfilledRolesForProject(PRIMARY_PROJECT_ID, roleIds);

        // THEN
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).deleteByProjectId(PRIMARY_PROJECT_ID);
        Mockito.verify(repository, Mockito.times( (wantedNumberOfInvocations = 2 ) )).save(Mockito.any(ProjectUnfilledRole.class));
    }

    @Test
    public void shouldFindByProjectIdReturnList() {
        // GIVEN
        Mockito.when(repository.findByProjectId(PRIMARY_PROJECT_ID))
                .thenReturn(List.of(unfilledRolePrimary, unfilledRoleSecondary));

        // WHEN
        List<ProjectUnfilledRole> result = service.findByProjectId(PRIMARY_PROJECT_ID);

        // THEN
        Assertions.assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(unfilledRolePrimary, unfilledRoleSecondary);

        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByProjectId(PRIMARY_PROJECT_ID);
    }

    @Test
    public void shouldFindUnfilledRolesByProjectIdReturnRoles() {
        // GIVEN
        Mockito.when(repository.findByProjectId(PRIMARY_PROJECT_ID))
                .thenReturn(List.of(unfilledRolePrimary, unfilledRoleSecondary));

        Role rolePrimary = new Role();
        rolePrimary.setId(PRIMARY_ROLE_ID);
        Role roleSecondary = new Role();
        roleSecondary.setId(SECONDARY_ROLE_ID);

        Mockito.when(roleRepository.findAllById(List.of(PRIMARY_ROLE_ID, SECONDARY_ROLE_ID)))
                .thenReturn(List.of(rolePrimary, roleSecondary));

        // WHEN
        List<Role> result = service.findUnfilledRolesByProjectId(PRIMARY_PROJECT_ID);

        // THEN
        Assertions.assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(rolePrimary, roleSecondary);

        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations)).findByProjectId(PRIMARY_PROJECT_ID);
        Mockito.verify(roleRepository, Mockito.times(wantedNumberOfInvocations))
                .findAllById(List.of(PRIMARY_ROLE_ID, SECONDARY_ROLE_ID));
    }

    @Test
    public void shouldFindUnfilledRolesByProjectIdsReturnMap() {
        // GIVEN
        List<ProjectUnfilledRole> allRoles = List.of(unfilledRolePrimary, unfilledRoleSecondary);
        Mockito.when(repository.findByProjectIdIn(List.of(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID)))
                .thenReturn(allRoles);

        Role rolePrimary = new Role();
        rolePrimary.setId(PRIMARY_ROLE_ID);
        Role roleSecondary = new Role();
        roleSecondary.setId(SECONDARY_ROLE_ID);

        Mockito.when(roleRepository.findAllById(List.of(PRIMARY_ROLE_ID, SECONDARY_ROLE_ID)))
                .thenReturn(List.of(rolePrimary, roleSecondary));

        // WHEN
        Map<UUID, List<Role>> result = service.findUnfilledRolesByProjectIds(List.of(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID));

        // THEN
        Assertions.assertThat(result)
                .hasSize(2)
                .containsKeys(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID);
        Assertions.assertThat(result.get(PRIMARY_PROJECT_ID))
                .containsExactlyInAnyOrder(rolePrimary);
        Assertions.assertThat(result.get(SECONDARY_PROJECT_ID))
                .containsExactlyInAnyOrder(roleSecondary);
    }

    @Test
    public void shouldGetProjectIdsByRoleIdsReturnSet() {
        // GIVEN
        Mockito.when(repository.findByRoleIdIn(List.of(PRIMARY_ROLE_ID, SECONDARY_ROLE_ID)))
                .thenReturn(List.of(unfilledRolePrimary, unfilledRoleSecondary));

        // WHEN
        Set<UUID> result = service.getProjectIdsByRoleIds(List.of(PRIMARY_ROLE_ID, SECONDARY_ROLE_ID));

        // THEN
        Assertions.assertThat(result)
                .containsExactlyInAnyOrder(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID);
    }

    @Test
    public void shouldGetRoleIdsByProjectReturnSet() {
        // GIVEN
        Mockito.when(repository.findByProjectId(PRIMARY_PROJECT_ID))
                .thenReturn(List.of(unfilledRolePrimary, unfilledRoleSecondary));

        // WHEN
        Set<Long> result = service.getRoleIdsByProject(PRIMARY_PROJECT_ID);

        // THEN
        Assertions.assertThat(result)
                .containsExactlyInAnyOrder(PRIMARY_ROLE_ID, SECONDARY_ROLE_ID);
    }

    @Test
    public void shouldRemoveRelationSucceed() {
        // WHEN
        service.removeRelation(PRIMARY_PROJECT_ID, PRIMARY_ROLE_ID);

        // THEN
        Mockito.verify(repository, Mockito.times(wantedNumberOfInvocations))
                .deleteByProjectIdAndRoleId(PRIMARY_PROJECT_ID, PRIMARY_ROLE_ID);
    }
}