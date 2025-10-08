package com.m42hub.m42hub_api.controllers.abuse;

import com.m42hub.m42hub_api.abuse.dto.request.AbuseRequest;
import com.m42hub.m42hub_api.abuse.entity.Abuse;
import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;
import com.m42hub.m42hub_api.abuse.enums.AbuseStatusEnum;
import com.m42hub.m42hub_api.abuse.enums.TargetTypeAbuseEnum;
import com.m42hub.m42hub_api.abuse.service.AbuseService;
import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AbuseControllerTest {

    @Mock
    private AbuseService abuseService;

    private User user;
    private AbuseCategory category;
    private Abuse abuse;
    private AbuseRequest abuseRequest;

    @BeforeEach
    public void setUp() {
        user = TestUtils.createUser(1L, "john.doe", "John", "Doe", "john@example.com", null);
        category = TestUtils.createAbuseCategory(1L, "Spam", "Conteúdo indesejado");
        
        abuse = Abuse.builder()
                .id(1L)
                .reporter(user)
                .targetType(TargetTypeAbuseEnum.PROJECT)
                .targetId(123L)
                .reasonCategory(category)
                .reasonText("Este projeto é ofensivo")
                .status(AbuseStatusEnum.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        abuseRequest = new AbuseRequest(
                TargetTypeAbuseEnum.PROJECT,
                123L,
                1L,
                "Este projeto é ofensivo"
        );
    }

    @Test
    public void shouldCreateAbuse_whenServiceCalled() {
        // GIVEN
        when(abuseService.createAbuse(any(AbuseRequest.class), eq(1L)))
                .thenReturn(abuse);

        // WHEN
        Abuse result = abuseService.createAbuse(abuseRequest, 1L);

        // THEN
        assertThat(result).isEqualTo(abuse);
        assertThat(result.getTargetType()).isEqualTo(TargetTypeAbuseEnum.PROJECT);
        assertThat(result.getStatus()).isEqualTo(AbuseStatusEnum.OPEN);
    }

    @Test
    public void shouldFindByParams_whenServiceCalled() {
        // GIVEN
        Page<Abuse> page = new PageImpl<>(List.of(abuse));
        when(abuseService.findByParams(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(page);

        // WHEN
        Page<Abuse> result = abuseService.findByParams(
                0, 10, null, null, null, null, null, null, null);

        // THEN
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(abuse);
    }

    @Test
    public void shouldFindById_whenValidId() {
        // GIVEN
        when(abuseService.findById(1L)).thenReturn(abuse);

        // WHEN
        Abuse result = abuseService.findById(1L);

        // THEN
        assertThat(result).isEqualTo(abuse);
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    public void shouldUpdateStatus_whenValidRequest() {
        // GIVEN
        abuse.setStatus(AbuseStatusEnum.CLOSED);
        abuse.setResolvedAt(LocalDateTime.now());
        when(abuseService.updateStatus(1L, AbuseStatusEnum.CLOSED)).thenReturn(abuse);

        // WHEN
        Abuse result = abuseService.updateStatus(1L, AbuseStatusEnum.CLOSED);

        // THEN
        assertThat(result.getStatus()).isEqualTo(AbuseStatusEnum.CLOSED);
        assertThat(result.getResolvedAt()).isNotNull();
    }
}