package com.m42hub.m42hub_api.services.abuse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.m42hub.m42hub_api.abuse.entity.Abuse;
import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;
import com.m42hub.m42hub_api.abuse.entity.AbuseStatus;
import com.m42hub.m42hub_api.abuse.enums.TargetTypeAbuseEnum;
import com.m42hub.m42hub_api.abuse.repository.AbuseRepository;
import com.m42hub.m42hub_api.abuse.service.AbuseCategoryService;
import com.m42hub.m42hub_api.abuse.service.AbuseService;
import com.m42hub.m42hub_api.abuse.service.AbuseStatusService;
import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.UserService;

public class AbuseServiceTest {

    private static final Long USER_ID = 1L;
    private static final String USERNAME = "john.doe";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String EMAIL = "john@example.com";

    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "SPAM";
    private static final String CATEGORY_DESC = "Conteúdo indesejado";

    private static final Long STATUS_ID = 1L;
    private static final String STATUS_NAME = "OPEN";
    private static final String STATUS_LABEL = "Aberta";
    private static final String STATUS_DESC = "Denúncia aberta";

    private static final Long CLOSED_STATUS_ID = 3L;

    private static final Long ABUSE_ID = 1L;
    private static final Long TARGET_ID = 123L;
    private static final String REASON_TEXT = "Este projeto é ofensivo";

    @Mock
    private AbuseRepository abuseRepository;

    @Mock
    private AbuseCategoryService abuseCategoryService;

    @Mock
    private AbuseStatusService abuseStatusService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AbuseService abuseService;

    private AutoCloseable mocks;

    private User user;
    private AbuseCategory category;
    private AbuseStatus status;
    private Abuse abuse;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        user = TestUtils.createUser(USER_ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, null);
        category = TestUtils.createAbuseCategory(CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESC);

        status = AbuseStatus.builder()
                .id(STATUS_ID)
                .name(STATUS_NAME)
                .label(STATUS_LABEL)
                .description(STATUS_DESC)
                .build();

        abuse = Abuse.builder()
                .id(ABUSE_ID)
                .reporter(user)
                .targetType(TargetTypeAbuseEnum.PROJECT)
                .targetId(TARGET_ID)
                .reasonCategory(category)
                .reasonText(REASON_TEXT)
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldCreateAbuse_whenValidRequest() {
        // GIVEN
        Mockito.when(userService.findById(USER_ID))
                .thenReturn(Optional.of(user));
        Mockito.when(abuseCategoryService.findById(CATEGORY_ID))
                .thenReturn(category);
        Mockito.when(abuseStatusService.findByName("OPEN"))
                .thenReturn(status);
        Mockito.when(abuseRepository.save(any(Abuse.class)))
                .thenReturn(abuse);

        // WHEN
        Abuse result = abuseService.createAbuse(abuse, USER_ID, CATEGORY_ID);

        // THEN
        assertThat(result)
                .isNotNull()
                .extracting(Abuse::getId, Abuse::getTargetType, Abuse::getTargetId)
                .containsExactly(ABUSE_ID, TargetTypeAbuseEnum.PROJECT, TARGET_ID);

        Mockito.verify(userService, Mockito.times(1)).findById(USER_ID);
        Mockito.verify(abuseCategoryService, Mockito.times(1)).findById(CATEGORY_ID);
        Mockito.verify(abuseStatusService, Mockito.times(1)).findByName("OPEN");
        Mockito.verify(abuseRepository, Mockito.times(1)).save(any(Abuse.class));
    }

    @Test
    public void shouldThrowException_whenUserNotFound() {
        // GIVEN
        Mockito.when(userService.findById(USER_ID))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> abuseService.createAbuse(abuse, USER_ID, CATEGORY_ID))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não encontrado");

        Mockito.verify(userService, Mockito.times(1)).findById(USER_ID);
        Mockito.verify(abuseCategoryService, Mockito.never()).findById(any());
        Mockito.verify(abuseRepository, Mockito.never()).save(any());
    }

    @Test
    public void shouldThrowException_whenCategoryNotFound() {
        // GIVEN
        Mockito.when(userService.findById(USER_ID))
                .thenReturn(Optional.of(user));
        Mockito.when(abuseCategoryService.findById(CATEGORY_ID))
                .thenThrow(new RuntimeException("Categoria não encontrada"));

        // WHEN & THEN
        assertThatThrownBy(() -> abuseService.createAbuse(abuse, USER_ID, CATEGORY_ID))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Categoria não encontrada");

        Mockito.verify(userService, Mockito.times(1)).findById(USER_ID);
        Mockito.verify(abuseCategoryService, Mockito.times(1)).findById(CATEGORY_ID);
        Mockito.verify(abuseRepository, Mockito.never()).save(any());
    }

    @Test
    public void shouldReturnAllAbuses_whenFindAllIsCalled() {
        // GIVEN
        List<Abuse> abuses = List.of(abuse);
        Mockito.when(abuseRepository.findAll()).thenReturn(abuses);

        // WHEN
        List<Abuse> result = abuseService.findAll();

        // THEN
        assertThat(result)
                .hasSize(1)
                .containsExactly(abuse);
        Mockito.verify(abuseRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnAbuse_whenFindByIdIsCalled() {
        // GIVEN
        Mockito.when(abuseRepository.findById(ABUSE_ID))
                .thenReturn(Optional.of(abuse));

        // WHEN
        Abuse result = abuseService.findById(ABUSE_ID);

        // THEN
        assertThat(result).isSameAs(abuse);
        Mockito.verify(abuseRepository, Mockito.times(1)).findById(ABUSE_ID);
    }

    @Test
    public void shouldThrowException_whenAbuseNotFound() {
        // GIVEN
        Long invalidId = 999L;
        Mockito.when(abuseRepository.findById(invalidId))
                .thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> abuseService.findById(invalidId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Denúncia não encontrada");

        Mockito.verify(abuseRepository, Mockito.times(1)).findById(invalidId);
    }

    @Test
    public void shouldUpdateStatus_whenValidStatusProvided() {
        // GIVEN
        AbuseStatus closedStatus = AbuseStatus.builder()
                .id(CLOSED_STATUS_ID)
                .name("CLOSED")
                .label("Fechada")
                .description("Denúncia fechada")
                .build();

        Mockito.when(abuseRepository.findById(ABUSE_ID))
                .thenReturn(Optional.of(abuse));
        Mockito.when(abuseStatusService.findById(CLOSED_STATUS_ID))
                .thenReturn(closedStatus);
        Mockito.when(abuseRepository.save(abuse))
                .thenReturn(abuse);

        // WHEN
        Abuse result = abuseService.updateStatus(ABUSE_ID, CLOSED_STATUS_ID);

        // THEN
        assertThat(result.getStatus().getName()).isEqualTo("CLOSED");
        assertThat(result.getResolvedAt()).isNotNull();

        Mockito.verify(abuseRepository, Mockito.times(1)).findById(ABUSE_ID);
        Mockito.verify(abuseStatusService, Mockito.times(1)).findById(CLOSED_STATUS_ID);
        Mockito.verify(abuseRepository, Mockito.times(1)).save(abuse);
    }

    @Test
    public void shouldReturnPagedAbuses_whenFindByParamsIsCalled() {
        // GIVEN
        List<Abuse> abuses = List.of(abuse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Abuse> page = new PageImpl<>(abuses, pageable, abuses.size());

        Mockito.when(abuseRepository.findAll(
                ArgumentMatchers.<Specification<Abuse>>any(),
                ArgumentMatchers.any(Pageable.class)))
                .thenReturn(page);

        // WHEN
        Page<Abuse> result = abuseService.findByParams(
                0, 10, "id", "ASC", null, null, null, null, null);

        // THEN
        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .contains(abuse);

        Mockito.verify(abuseRepository, Mockito.times(1))
                .findAll(ArgumentMatchers.<Specification<Abuse>>any(), any(Pageable.class));
    }
}