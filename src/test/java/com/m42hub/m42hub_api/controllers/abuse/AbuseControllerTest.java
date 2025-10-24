package com.m42hub.m42hub_api.controllers.abuse;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.m42hub.m42hub_api.abuse.controller.AbuseController;
import com.m42hub.m42hub_api.abuse.dto.request.AbuseRequest;
import com.m42hub.m42hub_api.abuse.entity.Abuse;
import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;
import com.m42hub.m42hub_api.abuse.entity.AbuseStatus;
import com.m42hub.m42hub_api.abuse.enums.TargetTypeAbuseEnum;
import com.m42hub.m42hub_api.abuse.service.AbuseService;
import com.m42hub.m42hub_api.config.JWTUserData;
import com.m42hub.m42hub_api.services.util.TestUtils;
import com.m42hub.m42hub_api.user.entity.User;

// REF: https://codemia.io/knowledge-hub/path/how_to_check_string_in_response_body_with_mockmvc
public class AbuseControllerTest {

    private static final String URI_TEMPLATE = "/api/v1/abuse";
    private static final String URI_TEMPLATE_ID = URI_TEMPLATE + "/{id}";
    private static final String URI_TEMPLATE_STATUS = URI_TEMPLATE + "/{id}/status";

    private static final Long PRIMARY_USER_ID = 1L;
    private static final String PRIMARY_USER_USERNAME = "john.doe";
    private static final String PRIMARY_USER_FIRST_NAME = "John";
    private static final String PRIMARY_USER_LAST_NAME = "Doe";
    private static final String PRIMARY_USER_EMAIL = "john@example.com";
    private static final String PRIMARY_USER_ROLE = "ADMIN";

    private static final Long PRIMARY_CATEGORY_ID = 1L;
    private static final String PRIMARY_CATEGORY_NAME = "SPAM";
    private static final String PRIMARY_CATEGORY_DESCRIPTION = "Conteúdo indesejado ou repetitivo";

    private static final Long PRIMARY_STATUS_ID = 1L;
    private static final String PRIMARY_STATUS_NAME = "OPEN";
    private static final String PRIMARY_STATUS_LABEL = "Aberta";
    private static final String PRIMARY_STATUS_DESCRIPTION = "Denúncia aberta";

    private static final Long CLOSED_STATUS_ID = 3L;
    private static final String CLOSED_STATUS_NAME = "CLOSED";
    private static final String CLOSED_STATUS_LABEL = "Fechada";
    private static final String CLOSED_STATUS_DESCRIPTION = "Denúncia fechada";

    private static final Long PRIMARY_ABUSE_ID = 1L;
    private static final Long PRIMARY_TARGET_ID = 1L;
    private static final TargetTypeAbuseEnum PRIMARY_TARGET_TYPE = TargetTypeAbuseEnum.PROJECT;
    private static final String PRIMARY_REASON_TEXT = "Este projeto é ofensivo";

    private static final String OPEN_STATUS_DISPLAY = "Aberta";
    private static final String CLOSED_STATUS_DISPLAY = "Fechada";
    private static final String PROJECT_TARGET_DISPLAY = "Project";

    private static final Long INVALID_ABUSE_ID = 999L;

    @Mock
    private AbuseService abuseService;

    @InjectMocks
    private AbuseController abuseController;

    private AutoCloseable mocks;
    private MockMvc mockMvc;

    private User primaryUser;
    private Abuse primaryAbuse;
    private AbuseCategory primaryCategory;
    private AbuseStatus primaryStatus;
    private AbuseStatus closedStatus;
    private AbuseRequest primaryAbuseRequest;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(abuseController).build();

        primaryUser = TestUtils.createUser(PRIMARY_USER_ID, PRIMARY_USER_USERNAME, PRIMARY_USER_FIRST_NAME, PRIMARY_USER_LAST_NAME, PRIMARY_USER_EMAIL, null);
        primaryCategory = TestUtils.createAbuseCategory(PRIMARY_CATEGORY_ID, PRIMARY_CATEGORY_NAME, PRIMARY_CATEGORY_DESCRIPTION);
        
        primaryStatus = AbuseStatus.builder()
                .id(PRIMARY_STATUS_ID)
                .name(PRIMARY_STATUS_NAME)
                .label(PRIMARY_STATUS_LABEL)
                .description(PRIMARY_STATUS_DESCRIPTION)
                .build();
                
        closedStatus = AbuseStatus.builder()
                .id(CLOSED_STATUS_ID)
                .name(CLOSED_STATUS_NAME)
                .label(CLOSED_STATUS_LABEL)
                .description(CLOSED_STATUS_DESCRIPTION)
                .build();

        primaryAbuse = Abuse.builder()
                .id(PRIMARY_ABUSE_ID)
                .reporter(primaryUser)
                .targetType(PRIMARY_TARGET_TYPE)
                .targetId(PRIMARY_TARGET_ID)
                .reasonCategory(primaryCategory)
                .reasonText(PRIMARY_REASON_TEXT)
                .status(primaryStatus)
                .createdAt(LocalDateTime.now())
                .build();

        primaryAbuseRequest = new AbuseRequest(PRIMARY_TARGET_TYPE, PRIMARY_TARGET_ID, PRIMARY_CATEGORY_ID, PRIMARY_REASON_TEXT);
        JWTUserData primaryJwtUserData = new JWTUserData(PRIMARY_USER_ID, PRIMARY_USER_USERNAME, PRIMARY_USER_ROLE);

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(primaryJwtUserData);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldCreateAbuseSuccessfully() throws Exception {
        // GIVEN
        Mockito.when(abuseService.createAbuse(Mockito.any(Abuse.class), Mockito.eq(PRIMARY_USER_ID), Mockito.eq(PRIMARY_CATEGORY_ID)))
                .thenReturn(primaryAbuse);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(primaryAbuseRequest);

        // WHEN
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post(URI_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // THEN
        resultActions
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(PRIMARY_ABUSE_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetType").value(PROJECT_TARGET_DISPLAY))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetId").value(PRIMARY_TARGET_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reasonText").value(PRIMARY_REASON_TEXT))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusLabel").value(OPEN_STATUS_DISPLAY));

        Mockito.verify(abuseService, Mockito.times(1)).createAbuse(Mockito.any(Abuse.class), Mockito.eq(PRIMARY_USER_ID), Mockito.eq(PRIMARY_CATEGORY_ID));
    }

    @Test
    public void shouldReturnAllAbuses_whenFindAllIsCalled() throws Exception {
        // GIVEN
        Page<Abuse> abusePage = new PageImpl<>(
                List.of(primaryAbuse),
                PageRequest.of(0, 10),
                1
        );
        Mockito.when(abuseService.findByParams(
                Mockito.eq(0), Mockito.eq(10), Mockito.isNull(), Mockito.isNull(),
                Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull()
        )).thenReturn(abusePage);

        // WHEN
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI_TEMPLATE)
                .param("page", "0")
                .param("limit", "10"));

        // THEN
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(11))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(PRIMARY_ABUSE_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].targetType").value(PRIMARY_TARGET_TYPE.getDisplayName()));

        Mockito.verify(abuseService, Mockito.times(1)).findByParams(
                Mockito.eq(0), Mockito.eq(10), Mockito.isNull(), Mockito.isNull(),
                Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull());
    }

    @Test
    public void shouldReturnAbuse_whenFindByIdIsCalled() throws Exception {
        // GIVEN
        Mockito.when(abuseService.findById(PRIMARY_ABUSE_ID)).thenReturn(primaryAbuse);

        // WHEN
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI_TEMPLATE_ID, PRIMARY_ABUSE_ID));

        // THEN
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(PRIMARY_ABUSE_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetType").value(PROJECT_TARGET_DISPLAY))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetId").value(PRIMARY_TARGET_ID));

        Mockito.verify(abuseService, Mockito.times(1)).findById(PRIMARY_ABUSE_ID);
    }

    @Test
    public void shouldReturnNotFound_whenFindByInvalidId() throws Exception {
        // GIVEN
        Mockito.when(abuseService.findById(INVALID_ABUSE_ID))
                .thenThrow(new RuntimeException("Denúncia não encontrada"));

        // WHEN
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI_TEMPLATE_ID, INVALID_ABUSE_ID));

        // THEN
        resultActions
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(abuseService, Mockito.times(1)).findById(INVALID_ABUSE_ID);
    }

    @Test
    public void shouldUpdateAbuseStatusSuccessfully() throws Exception {
        // GIVEN
        Abuse updatedAbuse = Abuse.builder()
                .id(PRIMARY_ABUSE_ID)
                .reporter(primaryUser)
                .targetType(PRIMARY_TARGET_TYPE)
                .targetId(PRIMARY_TARGET_ID)
                .reasonCategory(primaryCategory)
                .reasonText(PRIMARY_REASON_TEXT)
                .status(closedStatus)
                .createdAt(LocalDateTime.now())
                .resolvedAt(LocalDateTime.now())
                .build();

        Mockito.when(abuseService.updateStatus(PRIMARY_ABUSE_ID, CLOSED_STATUS_ID)).thenReturn(updatedAbuse);

        // WHEN
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.patch(URI_TEMPLATE_STATUS, PRIMARY_ABUSE_ID)
                .param("status", CLOSED_STATUS_ID.toString()));

        // THEN
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(PRIMARY_ABUSE_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusLabel").value(CLOSED_STATUS_DISPLAY));

        Mockito.verify(abuseService, Mockito.times(1)).updateStatus(PRIMARY_ABUSE_ID, CLOSED_STATUS_ID);
    }
}