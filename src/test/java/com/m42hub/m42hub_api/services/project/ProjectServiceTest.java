package com.m42hub.m42hub_api.services.project;

import com.m42hub.m42hub_api.file.service.ImgBBService;
import com.m42hub.m42hub_api.project.entity.Project;
import com.m42hub.m42hub_api.project.repository.ProjectRepository;
import com.m42hub.m42hub_api.project.service.*;
import com.m42hub.m42hub_api.services.util.TestUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

public class ProjectServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceTest.class);

    private final int wantedNumberOfInvocations = 1;

    private final UUID PRIMARY_PROJECT_ID = TestUtils.getRandomUUID();
    private final UUID SECONDARY_PROJECT_ID = TestUtils.getRandomUUID();
    private final UUID TERTIARY_PROJECT_ID = TestUtils.getRandomUUID();
    private final UUID PRIMARY_USER_ID = TestUtils.getRandomUUID();
    private final UUID NON_EXISTENT_PROJECT_ID = TestUtils.getRandomUUID();
    private static final Long PRIMARY_STATUS_ID = 1L;
    private static final Long PRIMARY_COMPLEXITY_ID = 1L;
    private static final Long PRIMARY_ROLE_ID = 1L;
    private static final Long INVALID_STATUS_ID = 1L;
    private static final Long FILTER_TOOL_ID = 1L;
    private static final Long FILTER_TOPIC_ID = 1L;

    private static final String PRIMARY_PROJECT_NAME = "Project Alpha";
    private static final String PRIMARY_PROJECT_SUMMARY = "First test project";
    private static final String PRIMARY_PROJECT_DESCRIPTION = "Description of project alpha";
    private static final String PRIMARY_PROJECT_IMAGE_URL = "http://example.com/image1.jpg";
    private static final String PRIMARY_PROJECT_DISCORD = "discord1";
    private static final String PRIMARY_PROJECT_GITHUB = "github1";
    private static final String PRIMARY_PROJECT_WEBSITE = "website1.com";

    private static final String SECONDARY_PROJECT_NAME = "Project Beta";
    private static final String SECONDARY_PROJECT_SUMMARY = "Second test project";
    private static final String SECONDARY_PROJECT_DESCRIPTION = "Description of project beta";
    private static final String SECONDARY_PROJECT_IMAGE_URL = "http://example.com/image2.jpg";
    private static final String SECONDARY_PROJECT_DISCORD = "discord2";
    private static final String SECONDARY_PROJECT_GITHUB = "github2";
    private static final String SECONDARY_PROJECT_WEBSITE = "website2.com";

    private static final String TERTIARY_PROJECT_NAME = "Project Gamma";
    private static final String TERTIARY_PROJECT_SUMMARY = "Third test project";
    private static final String TERTIARY_PROJECT_DESCRIPTION = "Description of project gamma";
    private static final String TERTIARY_PROJECT_IMAGE_URL = "http://example.com/image3.jpg";

    private static final String UPDATED_PROJECT_NAME = "Updated Project Alpha";
    private static final String UPDATED_PROJECT_SUMMARY = "Updated summary";
    private static final String UPDATED_PROJECT_DESCRIPTION = "Updated description";
    private static final String UPDATED_PROJECT_IMAGE_URL = "http://example.com/updated.jpg";
    private static final String UPDATED_PROJECT_DISCORD = "updated_discord";
    private static final String UPDATED_PROJECT_GITHUB = "updated_github";
    private static final String UPDATED_PROJECT_WEBSITE = "updated.com";

    private static final String NEW_PROJECT_NAME = "New Project";
    private static final String NEW_PROJECT_SUMMARY = "New summary";
    private static final String NEW_PROJECT_DESCRIPTION = "New description";
    private static final String NEW_PROJECT_IMAGE_URL = "http://example.com/new.jpg";
    private static final String NEW_PROJECT_DISCORD = "new_discord";
    private static final String NEW_PROJECT_GITHUB = "new_github";
    private static final String NEW_PROJECT_WEBSITE = "new.com";

    private static final String NEW_BANNER_URL = "http://example.com/new-banner.jpg";

    private static final Integer DEFAULT_PAGE = 0;
    private static final Integer DEFAULT_LIMIT = 10;
    private static final Integer SMALL_PAGE_SIZE = 2;
    private static final String SORT_BY_NAME = "name";
    private static final String SORT_DIRECTION_ASC = "asc";
    private static final String SORT_DIRECTION_DESC = "desc";

    @Mock
    private ProjectUnfilledRoleService projectUnfilledRoleService;

    @Mock
    private ProjectTopicService projectTopicService;

    @Mock
    private ProjectToolService projectToolService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ComplexityService complexityService;

    @Mock
    private StatusService statusService;

    @Mock
    private MemberService memberService;

    @Mock
    private ImgBBService imgBBService;

    @InjectMocks
    private ProjectService projectService;

    private AutoCloseable mocks;

    private Project primaryProject;
    private Project secondaryProject;
    private Project tertiaryProject;
    private Project updatedProject;
    private Project newProject;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        LocalDateTime now = LocalDateTime.now();
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + 86400000);

        primaryProject = TestUtils.createProject(
                PRIMARY_PROJECT_ID,
                PRIMARY_PROJECT_NAME,
                PRIMARY_PROJECT_SUMMARY,
                PRIMARY_PROJECT_DESCRIPTION,
                PRIMARY_STATUS_ID,
                PRIMARY_COMPLEXITY_ID,
                PRIMARY_PROJECT_IMAGE_URL,
                startDate,
                endDate,
                PRIMARY_PROJECT_DISCORD,
                PRIMARY_PROJECT_GITHUB,
                PRIMARY_PROJECT_WEBSITE,
                now,
                now
        );

        secondaryProject = TestUtils.createProject(
                SECONDARY_PROJECT_ID,
                SECONDARY_PROJECT_NAME,
                SECONDARY_PROJECT_SUMMARY,
                SECONDARY_PROJECT_DESCRIPTION,
                PRIMARY_STATUS_ID,
                PRIMARY_COMPLEXITY_ID,
                SECONDARY_PROJECT_IMAGE_URL,
                startDate,
                endDate,
                SECONDARY_PROJECT_DISCORD,
                SECONDARY_PROJECT_GITHUB,
                SECONDARY_PROJECT_WEBSITE,
                now,
                now
        );

        tertiaryProject = TestUtils.createProject(
                TERTIARY_PROJECT_ID,
                TERTIARY_PROJECT_NAME,
                TERTIARY_PROJECT_SUMMARY,
                TERTIARY_PROJECT_DESCRIPTION,
                PRIMARY_STATUS_ID,
                PRIMARY_COMPLEXITY_ID,
                TERTIARY_PROJECT_IMAGE_URL,
                startDate,
                endDate,
                PRIMARY_PROJECT_DISCORD,
                PRIMARY_PROJECT_GITHUB,
                PRIMARY_PROJECT_WEBSITE,
                now,
                now
        );

        updatedProject = TestUtils.createProject(
                PRIMARY_PROJECT_ID,
                UPDATED_PROJECT_NAME,
                UPDATED_PROJECT_SUMMARY,
                UPDATED_PROJECT_DESCRIPTION,
                PRIMARY_STATUS_ID,
                PRIMARY_COMPLEXITY_ID,
                UPDATED_PROJECT_IMAGE_URL,
                startDate,
                endDate,
                UPDATED_PROJECT_DISCORD,
                UPDATED_PROJECT_GITHUB,
                UPDATED_PROJECT_WEBSITE,
                now,
                now
        );

        newProject = TestUtils.createProject(
                null,
                NEW_PROJECT_NAME,
                NEW_PROJECT_SUMMARY,
                NEW_PROJECT_DESCRIPTION,
                PRIMARY_STATUS_ID,
                PRIMARY_COMPLEXITY_ID,
                NEW_PROJECT_IMAGE_URL,
                startDate,
                endDate,
                NEW_PROJECT_DISCORD,
                NEW_PROJECT_GITHUB,
                NEW_PROJECT_WEBSITE,
                now,
                now
        );
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldSaveProject() {
        // GIVEN
        Mockito.when(projectRepository.save(newProject)).thenReturn(primaryProject);

        // WHEN
        Project result = projectService.save(newProject);

        // THEN
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(PRIMARY_PROJECT_ID);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).save(newProject);
    }

    @Test
    public void shouldFindProjectById() {
        // GIVEN
        Mockito.when(projectRepository.findById(PRIMARY_PROJECT_ID)).thenReturn(Optional.of(primaryProject));

        // WHEN
        Optional<Project> result = projectService.findById(PRIMARY_PROJECT_ID);

        // THEN
        Assertions.assertThat(result).isPresent().containsSame(primaryProject);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).findById(PRIMARY_PROJECT_ID);
    }

    @Test
    public void shouldReturnEmpty_whenProjectNotFound() {
        // GIVEN
        UUID nonExistentId = TestUtils.getRandomUUID();
        Mockito.when(projectRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // WHEN
        Optional<Project> result = projectService.findById(nonExistentId);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).findById(nonExistentId);
    }

    @Test
    public void shouldFindAllProjects() {
        // GIVEN
        List<Project> expectedProjects = List.of(primaryProject, secondaryProject);
        Mockito.when(projectRepository.findAll()).thenReturn(expectedProjects);

        // WHEN
        List<Project> result = projectService.findAll();

        // THEN
        Assertions.assertThat(result).hasSize(2).containsExactlyInAnyOrder(primaryProject, secondaryProject);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).findAll();
    }

    @Test
    public void shouldUpdateProject_whenUserIsManager() {
        // GIVEN
        Mockito.when(projectRepository.findById(PRIMARY_PROJECT_ID)).thenReturn(Optional.of(primaryProject));
        Mockito.when(memberService.isUserProjectManager(PRIMARY_PROJECT_ID, PRIMARY_USER_ID)).thenReturn(true);
        Mockito.when(statusService.existsById(PRIMARY_STATUS_ID)).thenReturn(true);
        Mockito.when(complexityService.existsById(PRIMARY_COMPLEXITY_ID)).thenReturn(true);
        Mockito.when(projectRepository.save(primaryProject)).thenReturn(primaryProject);

        // WHEN
        Optional<Project> result = projectService.update(PRIMARY_PROJECT_ID, updatedProject, PRIMARY_USER_ID);

        // THEN
        Assertions.assertThat(result).isPresent().containsSame(primaryProject);
        Assertions.assertThat(primaryProject.getName()).isEqualTo(UPDATED_PROJECT_NAME);
        Assertions.assertThat(primaryProject.getSummary()).isEqualTo(UPDATED_PROJECT_SUMMARY);
        Assertions.assertThat(primaryProject.getDescription()).isEqualTo(UPDATED_PROJECT_DESCRIPTION);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).findById(PRIMARY_PROJECT_ID);
        Mockito.verify(memberService, Mockito.times(wantedNumberOfInvocations)).isUserProjectManager(PRIMARY_PROJECT_ID, PRIMARY_USER_ID);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).save(primaryProject);
    }

    @Test
    public void shouldThrowException_whenUpdatingProjectWithoutPermission() {
        // GIVEN
        Mockito.when(projectRepository.findById(PRIMARY_PROJECT_ID)).thenReturn(Optional.of(primaryProject));
        Mockito.when(memberService.isUserProjectManager(PRIMARY_PROJECT_ID, PRIMARY_USER_ID)).thenReturn(false);

        // WHEN
        ThrowableAssert.ThrowingCallable action = () -> projectService.update(PRIMARY_PROJECT_ID, updatedProject, PRIMARY_USER_ID);

        // THEN
        Assertions.assertThatThrownBy(action)
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Usuário não tem permissão para alterar o projeto");

        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).findById(PRIMARY_PROJECT_ID);
        Mockito.verify(memberService, Mockito.times(wantedNumberOfInvocations)).isUserProjectManager(PRIMARY_PROJECT_ID, PRIMARY_USER_ID);
        Mockito.verify(projectRepository, Mockito.never()).save(Mockito.any(Project.class));
    }

    @Test
    public void shouldChangeUnfilledRoles_whenUserIsManager() {
        // GIVEN
        List<Long> unfilledRoles = List.of(PRIMARY_ROLE_ID);

        Mockito.when(projectRepository.findById(PRIMARY_PROJECT_ID)).thenReturn(Optional.of(primaryProject));
        Mockito.when(memberService.isUserProjectManager(PRIMARY_PROJECT_ID, PRIMARY_USER_ID)).thenReturn(true);
        Mockito.doNothing().when(projectUnfilledRoleService).updateUnfilledRolesForProject(PRIMARY_PROJECT_ID, unfilledRoles);

        // WHEN
        Optional<Project> result = projectService.changeUnfilledRoles(PRIMARY_PROJECT_ID, unfilledRoles, PRIMARY_USER_ID);

        // THEN
        Assertions.assertThat(result).isPresent().containsSame(primaryProject);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).findById(PRIMARY_PROJECT_ID);
        Mockito.verify(memberService, Mockito.times(wantedNumberOfInvocations)).isUserProjectManager(PRIMARY_PROJECT_ID, PRIMARY_USER_ID);
        Mockito.verify(projectUnfilledRoleService, Mockito.times(wantedNumberOfInvocations)).updateUnfilledRolesForProject(PRIMARY_PROJECT_ID, unfilledRoles);
    }

    @Test
    public void shouldChangeProjectBanner_whenUserIsManager() {
        // GIVEN
        MultipartFile file = Mockito.mock(MultipartFile.class);

        Mockito.when(projectRepository.findById(PRIMARY_PROJECT_ID)).thenReturn(Optional.of(primaryProject));
        Mockito.when(memberService.isUserProjectManager(PRIMARY_PROJECT_ID, PRIMARY_USER_ID)).thenReturn(true);
        Mockito.when(imgBBService.uploadImage(file)).thenReturn(NEW_BANNER_URL);
        Mockito.when(projectRepository.save(primaryProject)).thenReturn(primaryProject);

        // WHEN
        Optional<Project> result = projectService.changeProjectBanner(file, PRIMARY_PROJECT_ID, PRIMARY_USER_ID);

        // THEN
        Assertions.assertThat(result).isPresent().containsSame(primaryProject);
        Assertions.assertThat(primaryProject.getImageUrl()).isEqualTo(NEW_BANNER_URL);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).findById(PRIMARY_PROJECT_ID);
        Mockito.verify(memberService, Mockito.times(wantedNumberOfInvocations)).isUserProjectManager(PRIMARY_PROJECT_ID, PRIMARY_USER_ID);
        Mockito.verify(imgBBService, Mockito.times(wantedNumberOfInvocations)).uploadImage(file);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).save(primaryProject);
    }

    @Test
    public void shouldFindAllProjectsByIds() {
        // GIVEN
        List<UUID> projectIds = List.of(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID);
        List<Project> expectedProjects = List.of(primaryProject, secondaryProject);
        Mockito.when(projectRepository.findAllById(projectIds)).thenReturn(expectedProjects);

        // WHEN
        Map<UUID, Project> result = projectService.findAllByIds(projectIds);

        // THEN
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result).containsKeys(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID);
        Assertions.assertThat(result).containsValues(primaryProject, secondaryProject);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).findAllById(projectIds);
    }

    @Test
    public void shouldReturnEmptyMap_whenFindAllByIdsWithEmptyList() {
        // GIVEN
        List<UUID> emptyProjectIds = Collections.emptyList();
        Mockito.when(projectRepository.findAllById(emptyProjectIds)).thenReturn(Collections.emptyList());

        // WHEN
        Map<UUID, Project> result = projectService.findAllByIds(emptyProjectIds);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).findAllById(emptyProjectIds);
    }

    @Test
    public void shouldFindByParamsWithStatusFilter() {
        // GIVEN
        List<Long> statusIds = List.of(PRIMARY_STATUS_ID);
        Set<UUID> filteredProjectIds = Set.of(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID);

        Mockito.when(projectRepository.findByStatusIdIn(statusIds))
                .thenReturn(List.of(primaryProject, secondaryProject));
        Mockito.when(projectRepository.findAllById(filteredProjectIds))
                .thenReturn(List.of(primaryProject, secondaryProject));

        // WHEN
        Page<Project> result = projectService.findByParams(
                DEFAULT_PAGE,
                DEFAULT_LIMIT,
                SORT_BY_NAME,
                SORT_DIRECTION_ASC,
                statusIds,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );

        // THEN
        Assertions.assertThat(result.getContent()).hasSize(2);
        Assertions.assertThat(result.getContent()).containsExactly(primaryProject, secondaryProject);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).findByStatusIdIn(statusIds);
    }

    @Test
    public void shouldFindByParamsWithMultipleFilters() {
        // GIVEN
        List<Long> statusIds = List.of(PRIMARY_STATUS_ID);
        List<Long> complexityIds = List.of(PRIMARY_COMPLEXITY_ID);
        List<Long> toolIds = List.of(FILTER_TOOL_ID);
        List<Long> topicIds = List.of(FILTER_TOPIC_ID);
        List<Long> roleIds = List.of(PRIMARY_ROLE_ID);

        Mockito.when(projectRepository.findByStatusIdIn(statusIds))
                .thenReturn(List.of(primaryProject, secondaryProject));

        Mockito.when(projectRepository.findByComplexityIdIn(complexityIds))
                .thenReturn(List.of(primaryProject));

        Mockito.when(projectToolService.getProjectIdsByToolIds(toolIds))
                .thenReturn(Set.of(PRIMARY_PROJECT_ID, TERTIARY_PROJECT_ID));

        Mockito.when(projectTopicService.getProjectIdsByTopicIds(topicIds))
                .thenReturn(Set.of(PRIMARY_PROJECT_ID));

        Mockito.when(projectUnfilledRoleService.getProjectIdsByRoleIds(roleIds))
                .thenReturn(Set.of(PRIMARY_PROJECT_ID));

        Mockito.when(projectRepository.findAllById(Set.of(PRIMARY_PROJECT_ID)))
                .thenReturn(List.of(primaryProject));

        // WHEN
        Page<Project> result = projectService.findByParams(
                DEFAULT_PAGE,
                DEFAULT_LIMIT,
                SORT_BY_NAME,
                SORT_DIRECTION_ASC,
                statusIds,
                complexityIds,
                toolIds,
                topicIds,
                roleIds
        );

        // THEN
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent()).containsExactly(primaryProject);
    }

    @Test
    public void shouldUpdateProjectPartially() {
        // GIVEN
        Project partialUpdate = TestUtils.createProject(
                PRIMARY_PROJECT_ID,
                UPDATED_PROJECT_NAME,
                null,
                UPDATED_PROJECT_DESCRIPTION,
                null,
                null,
                null,
                null,
                null,
                UPDATED_PROJECT_DISCORD,
                null,
                null,
                null,
                null
        );

        Mockito.when(projectRepository.findById(PRIMARY_PROJECT_ID)).thenReturn(Optional.of(primaryProject));
        Mockito.when(memberService.isUserProjectManager(PRIMARY_PROJECT_ID, PRIMARY_USER_ID)).thenReturn(true);
        Mockito.when(projectRepository.save(primaryProject)).thenReturn(primaryProject);

        // WHEN
        Optional<Project> result = projectService.update(PRIMARY_PROJECT_ID, partialUpdate, PRIMARY_USER_ID);

        // THEN
        Assertions.assertThat(result).isPresent().containsSame(primaryProject);
        Assertions.assertThat(primaryProject.getName()).isEqualTo(UPDATED_PROJECT_NAME);
        Assertions.assertThat(primaryProject.getDescription()).isEqualTo(UPDATED_PROJECT_DESCRIPTION);
        Assertions.assertThat(primaryProject.getDiscord()).isEqualTo(UPDATED_PROJECT_DISCORD);
        Assertions.assertThat(primaryProject.getSummary()).isEqualTo(PRIMARY_PROJECT_SUMMARY);
        Assertions.assertThat(primaryProject.getGithub()).isEqualTo(PRIMARY_PROJECT_GITHUB);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).save(primaryProject);
    }

    @Test
    public void shouldNotUpdateProjectWithInvalidStatus() {
        // GIVEN
        Project updateWithInvalidStatus = TestUtils.createProject(
                PRIMARY_PROJECT_ID,
                PRIMARY_PROJECT_NAME,
                PRIMARY_PROJECT_SUMMARY,
                PRIMARY_PROJECT_DESCRIPTION,
                INVALID_STATUS_ID,
                PRIMARY_COMPLEXITY_ID,
                PRIMARY_PROJECT_IMAGE_URL,
                primaryProject.getStartDate(),
                primaryProject.getEndDate(),
                PRIMARY_PROJECT_DISCORD,
                PRIMARY_PROJECT_GITHUB,
                PRIMARY_PROJECT_WEBSITE,
                primaryProject.getCreatedAt(),
                primaryProject.getUpdatedAt()
        );

        Mockito.when(projectRepository.findById(PRIMARY_PROJECT_ID)).thenReturn(Optional.of(primaryProject));
        Mockito.when(memberService.isUserProjectManager(PRIMARY_PROJECT_ID, PRIMARY_USER_ID)).thenReturn(true);
        Mockito.when(statusService.existsById(INVALID_STATUS_ID)).thenReturn(false);
        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(primaryProject);

        // WHEN
        Optional<Project> result = projectService.update(PRIMARY_PROJECT_ID, updateWithInvalidStatus, PRIMARY_USER_ID);

        // THEN
        Assertions.assertThat(result).isPresent().containsSame(primaryProject);
        Assertions.assertThat(primaryProject.getStatusId()).isEqualTo(PRIMARY_STATUS_ID);
        Mockito.verify(statusService, Mockito.times(wantedNumberOfInvocations)).existsById(INVALID_STATUS_ID);
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).save(primaryProject);
    }

    @Test
    public void shouldReturnEmpty_whenChangingBannerForNonExistentProject() {
        // GIVEN
        MultipartFile file = Mockito.mock(MultipartFile.class);

        Mockito.when(projectRepository.findById(NON_EXISTENT_PROJECT_ID)).thenReturn(Optional.empty());

        // WHEN
        Optional<Project> result = projectService.changeProjectBanner(file, NON_EXISTENT_PROJECT_ID, PRIMARY_USER_ID);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(projectRepository, Mockito.times(wantedNumberOfInvocations)).findById(NON_EXISTENT_PROJECT_ID);
        Mockito.verify(memberService, Mockito.never()).isUserProjectManager(Mockito.any(), Mockito.any());
        Mockito.verify(imgBBService, Mockito.never()).uploadImage(file);
        Mockito.verify(projectRepository, Mockito.never()).save(Mockito.any(Project.class));
    }


    @Test
    public void shouldFindByParamsWithNoFilters() {
        // GIVEN
        List<Project> allProjects = List.of(primaryProject, secondaryProject, tertiaryProject);
        Set<UUID> allProjectIds = Set.of(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID, TERTIARY_PROJECT_ID);

        Mockito.when(projectRepository.findAll()).thenReturn(allProjects);
        Mockito.when(projectRepository.findAllById(allProjectIds)).thenReturn(allProjects);

        // WHEN
        Page<Project> result = projectService.findByParams(
                DEFAULT_PAGE,
                DEFAULT_LIMIT,
                SORT_BY_NAME,
                SORT_DIRECTION_ASC,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );

        // THEN
        Assertions.assertThat(result.getContent()).hasSize(3);
        Assertions.assertThat(result.getContent()).containsExactlyInAnyOrder(primaryProject, secondaryProject, tertiaryProject);
        Mockito.verify(projectRepository, Mockito.times(1)).findAll();
        Mockito.verify(projectRepository, Mockito.times(1)).findAllById(allProjectIds);
    }

    @Test
    public void shouldFindByParamsWithComplexityFilter() {
        // GIVEN
        List<Long> complexityIds = List.of(PRIMARY_COMPLEXITY_ID);
        Set<UUID> filteredProjectIds = Set.of(PRIMARY_PROJECT_ID);

        Mockito.when(projectRepository.findByComplexityIdIn(complexityIds))
                .thenReturn(List.of(primaryProject));
        Mockito.when(projectRepository.findAllById(filteredProjectIds))
                .thenReturn(List.of(primaryProject));

        // WHEN
        Page<Project> result = projectService.findByParams(
                DEFAULT_PAGE,
                DEFAULT_LIMIT,
                SORT_BY_NAME,
                SORT_DIRECTION_ASC,
                Collections.emptyList(),
                complexityIds,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );

        // THEN
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent()).containsExactly(primaryProject);
        Mockito.verify(projectRepository, Mockito.times(1)).findByComplexityIdIn(complexityIds);
    }

    @Test
    public void shouldFindByParamsWithToolsFilter() {
        // GIVEN
        List<Long> toolIds = List.of(FILTER_TOOL_ID);
        Set<UUID> filteredProjectIds = Set.of(PRIMARY_PROJECT_ID, TERTIARY_PROJECT_ID);

        Mockito.when(projectToolService.getProjectIdsByToolIds(toolIds))
                .thenReturn(filteredProjectIds);
        Mockito.when(projectRepository.findAllById(filteredProjectIds))
                .thenReturn(List.of(primaryProject, tertiaryProject));

        // WHEN
        Page<Project> result = projectService.findByParams(
                DEFAULT_PAGE,
                DEFAULT_LIMIT,
                SORT_BY_NAME,
                SORT_DIRECTION_ASC,
                Collections.emptyList(),
                Collections.emptyList(),
                toolIds,
                Collections.emptyList(),
                Collections.emptyList()
        );

        // THEN
        Assertions.assertThat(result.getContent()).hasSize(2);
        Assertions.assertThat(result.getContent()).containsExactlyInAnyOrder(primaryProject, tertiaryProject);
        Mockito.verify(projectToolService, Mockito.times(1)).getProjectIdsByToolIds(toolIds);
    }

    @Test
    public void shouldFindByParamsWithTopicsFilter() {
        // GIVEN
        List<Long> topicIds = List.of(FILTER_TOPIC_ID);
        Set<UUID> filteredProjectIds = Set.of(PRIMARY_PROJECT_ID);

        Mockito.when(projectTopicService.getProjectIdsByTopicIds(topicIds))
                .thenReturn(filteredProjectIds);
        Mockito.when(projectRepository.findAllById(filteredProjectIds))
                .thenReturn(List.of(primaryProject));

        // WHEN
        Page<Project> result = projectService.findByParams(
                DEFAULT_PAGE,
                DEFAULT_LIMIT,
                SORT_BY_NAME,
                SORT_DIRECTION_ASC,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                topicIds,
                Collections.emptyList()
        );

        // THEN
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent()).containsExactly(primaryProject);
        Mockito.verify(projectTopicService, Mockito.times(1)).getProjectIdsByTopicIds(topicIds);
    }

    @Test
    public void shouldFindByParamsWithUnfilledRolesFilter() {
        // GIVEN
        List<Long> roleIds = List.of(PRIMARY_ROLE_ID);
        Set<UUID> filteredProjectIds = Set.of(PRIMARY_PROJECT_ID);

        Mockito.when(projectUnfilledRoleService.getProjectIdsByRoleIds(roleIds))
                .thenReturn(filteredProjectIds);
        Mockito.when(projectRepository.findAllById(filteredProjectIds))
                .thenReturn(List.of(primaryProject));

        // WHEN
        Page<Project> result = projectService.findByParams(
                DEFAULT_PAGE,
                DEFAULT_LIMIT,
                SORT_BY_NAME,
                SORT_DIRECTION_ASC,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                roleIds
        );

        // THEN
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent()).containsExactly(primaryProject);
        Mockito.verify(projectUnfilledRoleService, Mockito.times(1)).getProjectIdsByRoleIds(roleIds);
    }

    @Test
    public void shouldReturnEmpty_whenFiltersHaveNoMatches() {
        // GIVEN
        List<Long> statusIds = List.of(PRIMARY_STATUS_ID);
        List<Long> toolIds = List.of(FILTER_TOOL_ID);

        Mockito.when(projectRepository.findByStatusIdIn(statusIds))
                .thenReturn(List.of(primaryProject));
        Mockito.when(projectToolService.getProjectIdsByToolIds(toolIds))
                .thenReturn(Set.of(SECONDARY_PROJECT_ID));

        // WHEN
        Page<Project> result = projectService.findByParams(
                DEFAULT_PAGE,
                DEFAULT_LIMIT,
                SORT_BY_NAME,
                SORT_DIRECTION_ASC,
                statusIds,
                Collections.emptyList(),
                toolIds,
                Collections.emptyList(),
                Collections.emptyList()
        );

        // THEN
        Assertions.assertThat(result.getContent()).isEmpty();
        Assertions.assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    public void shouldFindByParamsWithSorting() {
        // GIVEN
        List<Project> allProjects = List.of(primaryProject, secondaryProject, tertiaryProject);
        Set<UUID> allProjectIds = Set.of(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID, TERTIARY_PROJECT_ID);

        Mockito.when(projectRepository.findAll()).thenReturn(allProjects);
        Mockito.when(projectRepository.findAllById(allProjectIds)).thenReturn(allProjects);

        // WHEN
        Page<Project> result = projectService.findByParams(
                DEFAULT_PAGE,
                DEFAULT_LIMIT,
                SORT_BY_NAME,
                SORT_DIRECTION_DESC,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );

        // THEN
        Assertions.assertThat(result.getContent()).hasSize(3);
        Assertions.assertThat(result.getContent().get(0).getName()).isEqualTo(TERTIARY_PROJECT_NAME);
        Assertions.assertThat(result.getContent().get(1).getName()).isEqualTo(SECONDARY_PROJECT_NAME);
        Assertions.assertThat(result.getContent().get(2).getName()).isEqualTo(PRIMARY_PROJECT_NAME);
    }

    @Test
    public void shouldFindByParamsWithPagination() {
        // GIVEN
        List<Project> allProjects = List.of(primaryProject, secondaryProject, tertiaryProject);
        Set<UUID> allProjectIds = Set.of(PRIMARY_PROJECT_ID, SECONDARY_PROJECT_ID, TERTIARY_PROJECT_ID);

        Mockito.when(projectRepository.findAll()).thenReturn(allProjects);
        Mockito.when(projectRepository.findAllById(allProjectIds)).thenReturn(allProjects);

        // WHEN
        Page<Project> result = projectService.findByParams(
                DEFAULT_PAGE,
                SMALL_PAGE_SIZE,
                SORT_BY_NAME,
                SORT_DIRECTION_ASC,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );

        // THEN
        Assertions.assertThat(result.getContent()).hasSize(2);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(result.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(result.getContent()).containsExactly(primaryProject, secondaryProject);
    }
}