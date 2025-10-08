package com.m42hub.m42hub_api.services.user;

import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.repository.SystemRoleRepository;
import com.m42hub.m42hub_api.user.repository.UserRepository;
import com.m42hub.m42hub_api.user.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceTests {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceTests.class);

    private final int wantedNumberOfInvocations = 1;

    private final UUID ADMIN_ROLE_ID = TestUtils.getRandomUUID();
    private static final String ADMIN_ROLE_NAME = "ADMIN";

    private final UUID USER_ROLE_ID = TestUtils.getRandomUUID();

    private final UUID JOHN_ID = TestUtils.getRandomUUID();
    private static final String JOHN_USERNAME = "john.doe";
    private static final String JOHN_FIRST_NAME = "John";
    private static final String JOHN_LAST_NAME = "Doe";
    private static final String JOHN_EMAIL = "john@example.com";
    private static final String JOHN_PASSWORD = "pass1";
    private static final String JOHN_PROFILE_PIC = "pic1.jpg";
    private static final String JOHN_BANNER = "banner1.jpg";
    private static final String JOHN_BIO = "Bio John";
    private static final String JOHN_DISCORD = "discord1";
    private static final String JOHN_LINKEDIN = "linkedin1";
    private static final String JOHN_GITHUB = "github1";
    private static final String JOHN_WEBSITE = "website1.com";
    private static final Boolean JOHN_ACTIVE = true;

    private final UUID JANE_ID = TestUtils.getRandomUUID();
    private static final String JANE_USERNAME = "jane.smith";
    private static final String JANE_FIRST_NAME = "Jane";
    private static final String JANE_LAST_NAME = "Smith";
    private static final String JANE_EMAIL = "jane@example.com";
    private static final String JANE_PASSWORD = "pass2";
    private static final String JANE_PROFILE_PIC = "pic2.jpg";
    private static final String JANE_BANNER = "banner2.jpg";
    private static final String JANE_BIO = "Bio Jane";
    private static final String JANE_DISCORD = "discord2";
    private static final String JANE_LINKEDIN = "linkedin2";
    private static final String JANE_GITHUB = "github2";
    private static final String JANE_WEBSITE = "website2.com";
    private static final Boolean JANE_ACTIVE = true;

    private final UUID NEW_USER_ID = TestUtils.getRandomUUID();
    private static final String NEW_USER_USERNAME = "new.user";
    private static final String NEW_USER_FIRST_NAME = "New";
    private static final String NEW_USER_LAST_NAME = "User";
    private static final String NEW_USER_EMAIL = "new@example.com";
    private static final String NEW_USER_PROFILE_PIC = "pic3.jpg";
    private static final String NEW_USER_BANNER = "banner3.jpg";
    private static final String NEW_USER_BIO = "Bio New";
    private static final String NEW_USER_DISCORD = "discord3";
    private static final String NEW_USER_LINKEDIN = "linkedin3";
    private static final String NEW_USER_GITHUB = "github3";
    private static final String NEW_USER_WEBSITE = "website3.com";
    private static final Boolean NEW_USER_ACTIVE = true;
    private static final String RAW_PASSWORD = "password123";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SystemRoleRepository systemRoleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private AutoCloseable mocks;

    private SystemRole adminRole;
    private User john;
    private User jane;
    private User newUser;
    private User savedUser;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        adminRole = TestUtils.createRole(ADMIN_ROLE_ID, ADMIN_ROLE_NAME);

        LocalDateTime now = LocalDateTime.now();

        john = TestUtils.createUser(
                JOHN_ID,
                JOHN_USERNAME,
                JOHN_FIRST_NAME,
                JOHN_LAST_NAME,
                JOHN_EMAIL,
                JOHN_PASSWORD,
                JOHN_PROFILE_PIC,
                JOHN_BANNER,
                JOHN_BIO,
                JOHN_DISCORD,
                JOHN_LINKEDIN,
                JOHN_GITHUB,
                JOHN_WEBSITE,
                JOHN_ACTIVE,
                now,
                now,
                now
        );
        john.setSystemRoleId(ADMIN_ROLE_ID);

        jane = TestUtils.createUser(
                JANE_ID,
                JANE_USERNAME,
                JANE_FIRST_NAME,
                JANE_LAST_NAME,
                JANE_EMAIL,
                JANE_PASSWORD,
                JANE_PROFILE_PIC,
                JANE_BANNER,
                JANE_BIO,
                JANE_DISCORD,
                JANE_LINKEDIN,
                JANE_GITHUB,
                JANE_WEBSITE,
                JANE_ACTIVE,
                now,
                now,
                now
        );
        jane.setSystemRoleId(USER_ROLE_ID);

        newUser = TestUtils.createUser(
                null,
                NEW_USER_USERNAME,
                NEW_USER_FIRST_NAME,
                NEW_USER_LAST_NAME,
                NEW_USER_EMAIL,
                RAW_PASSWORD,
                NEW_USER_PROFILE_PIC,
                NEW_USER_BANNER,
                NEW_USER_BIO,
                NEW_USER_DISCORD,
                NEW_USER_LINKEDIN,
                NEW_USER_GITHUB,
                NEW_USER_WEBSITE,
                NEW_USER_ACTIVE,
                now,
                now,
                now
        );
        newUser.setSystemRoleId(ADMIN_ROLE_ID);

        savedUser = TestUtils.createUser(
                NEW_USER_ID,
                NEW_USER_USERNAME,
                NEW_USER_FIRST_NAME,
                NEW_USER_LAST_NAME,
                NEW_USER_EMAIL,
                ENCODED_PASSWORD,
                NEW_USER_PROFILE_PIC,
                NEW_USER_BANNER,
                NEW_USER_BIO,
                NEW_USER_DISCORD,
                NEW_USER_LINKEDIN,
                NEW_USER_GITHUB,
                NEW_USER_WEBSITE,
                NEW_USER_ACTIVE,
                now,
                now,
                now
        );
        savedUser.setSystemRoleId(ADMIN_ROLE_ID);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllUsers_whenFindAllIsCalled() {
        // GIVEN
        List<User> expectedUsers = List.of(john, jane);
        Mockito.when(userRepository.findAll()).thenReturn(expectedUsers);

        // WHEN
        List<User> result = userService.findAll();

        // THEN
        Assertions.assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(john, jane);
        Mockito.verify(userRepository, Mockito.times(wantedNumberOfInvocations)).findAll();
    }

    @Test
    public void shouldReturnUser_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(userRepository.findById(JOHN_ID))
                .thenReturn(Optional.of(john));

        // WHEN
        Optional<User> result = userService.findById(JOHN_ID);

        // THEN
        Assertions.assertThat(result)
                .isPresent()
                .containsSame(john);
        Mockito.verify(userRepository, Mockito.times(wantedNumberOfInvocations)).findById(JOHN_ID);
    }

    @Test
    public void shouldReturnEmpty_whenFindByInvalidId() {
        // GIVEN
        UUID invalidId = TestUtils.getRandomUUID();
        Mockito.when(userRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN
        Optional<User> result = userService.findById(invalidId);

        // THEN
        Assertions.assertThat(result).isEmpty();
        Mockito.verify(userRepository, Mockito.times(wantedNumberOfInvocations)).findById(invalidId);
    }

    @Test
    public void shouldSaveUserSucceed() {
        // GIVEN
        Mockito.when(passwordEncoder.encode(RAW_PASSWORD))
                .thenReturn(ENCODED_PASSWORD);
        Mockito.when(systemRoleRepository.findById(ADMIN_ROLE_ID))
                .thenReturn(Optional.of(adminRole));
        Mockito.when(userRepository.save(newUser))
                .thenReturn(savedUser);

        // WHEN
        UserService.UserSaveResult result = userService.saveWithRole(newUser);

        // THEN
        Assertions.assertThat(result.user())
                .isNotNull()
                .extracting(User::getId, User::getUsername)
                .containsExactly(NEW_USER_ID, NEW_USER_USERNAME);

        Assertions.assertThat(newUser.getPassword()).isEqualTo(ENCODED_PASSWORD);
        Mockito.verify(passwordEncoder, Mockito.times(wantedNumberOfInvocations)).encode(RAW_PASSWORD);
        Mockito.verify(systemRoleRepository, Mockito.times(wantedNumberOfInvocations)).findById(ADMIN_ROLE_ID);
        Mockito.verify(userRepository, Mockito.times(wantedNumberOfInvocations)).save(newUser);
    }
}