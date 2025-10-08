package com.m42hub.m42hub_api.services.user;

import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.repository.SystemRoleRepository;
import com.m42hub.m42hub_api.user.service.SystemRoleService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SystemRoleServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(SystemRoleServiceTest.class);

    private final int wantedNumberOfInvocations = 1;

    private final UUID ADMIN_ROLE_ID = TestUtils.getRandomUUID();
    private static final String ADMIN_ROLE_NAME = "ADMIN";

    private final UUID USER_ROLE_ID = TestUtils.getRandomUUID();
    private static final String USER_ROLE_NAME = "USER";

    private final UUID NEW_ROLE_ID = TestUtils.getRandomUUID();
    private static final String NEW_ROLE_NAME = "MODERATOR";

    @Mock
    private SystemRoleRepository systemRoleRepository;

    @InjectMocks
    private SystemRoleService systemRoleService;

    private AutoCloseable mocks;

    private SystemRole adminRole;
    private SystemRole userRole;
    private SystemRole newRole;
    private SystemRole savedRole;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        adminRole = TestUtils.createRole(ADMIN_ROLE_ID, ADMIN_ROLE_NAME);
        userRole = TestUtils.createRole(USER_ROLE_ID, USER_ROLE_NAME);

        newRole = TestUtils.createRole(null, NEW_ROLE_NAME);
        savedRole = TestUtils.createRole(NEW_ROLE_ID, NEW_ROLE_NAME);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllSystemRoles_whenFindAllIsCalled() {
        // GIVEN
        List<SystemRole> expectedRoles = List.of(adminRole, userRole);
        Mockito.when(systemRoleRepository.findAll()).thenReturn(expectedRoles);

        // WHEN
        List<SystemRole> result = systemRoleService.findAll();

        // THEN
        Assertions.assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(adminRole, userRole);
        Mockito.verify(systemRoleRepository, Mockito.times(wantedNumberOfInvocations)).findAll();
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        UUID invalidId = TestUtils.getRandomUUID();
        Mockito.when(systemRoleRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<SystemRole> result = systemRoleService.findById(invalidId);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(systemRoleRepository, Mockito.times(wantedNumberOfInvocations)).findById(invalidId);
    }

    @Test
    public void shouldSaveSystemRoleSucceed() {
        // GIVEN
        Mockito.when(systemRoleRepository.save(newRole))
                .thenReturn(savedRole);

        // WHEN
        SystemRole result = systemRoleService.save(newRole);

        // THEN
        Assertions.assertThat(result)
                .isNotNull()
                .extracting(SystemRole::getId, SystemRole::getName)
                .containsExactly(NEW_ROLE_ID, NEW_ROLE_NAME);
        Mockito.verify(systemRoleRepository, Mockito.times(wantedNumberOfInvocations)).save(newRole);
    }
}