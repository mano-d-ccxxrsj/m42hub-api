package com.m42hub.m42hub_api.controllers.abuse;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.m42hub.m42hub_api.abuse.controller.AbuseCategoryController;
import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;
import com.m42hub.m42hub_api.abuse.service.AbuseCategoryService;

// REF: https://howtodoinjava.com/spring-boot2/testing/rest-controller-unit-test-example/
public class AbuseCategoryControllerTest {

    private static final String URI_TEMPLATE = "/api/v1/abuse/category";

    private static final Long SPAM_CATEGORY_ID = 1L;
    private static final String SPAM_CATEGORY_NAME = "Spam";
    private static final String SPAM_CATEGORY_DESCRIPTION = "Conteúdo indesejado ou repetitivo";

    private static final Long ABUSE_CATEGORY_ID = 2L;
    private static final String ABUSE_CATEGORY_NAME = "Abuso";
    private static final String ABUSE_CATEGORY_DESCRIPTION = "Linguagem ofensiva ou comportamento abusivo";

    @Mock
    private AbuseCategoryService abuseCategoryService;

    @InjectMocks
    private AbuseCategoryController abuseCategoryController;

    private AbuseCategory spamCategory;
    private AbuseCategory abuseCategory;

    private AutoCloseable mocks;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(abuseCategoryController).build();

        spamCategory = AbuseCategory.builder()
                .id(SPAM_CATEGORY_ID)
                .name(SPAM_CATEGORY_NAME)
                .description(SPAM_CATEGORY_DESCRIPTION)
                .build();

        abuseCategory = AbuseCategory.builder()
                .id(ABUSE_CATEGORY_ID)
                .name(ABUSE_CATEGORY_NAME)
                .description(ABUSE_CATEGORY_DESCRIPTION)
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllAbuseCategories() throws Exception {
        // GIVEN
        Mockito.when(abuseCategoryService.findAll()).thenReturn(List.of(spamCategory, abuseCategory));

        // WHEN
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI_TEMPLATE));

        // THEN
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(SPAM_CATEGORY_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(SPAM_CATEGORY_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(SPAM_CATEGORY_DESCRIPTION))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(ABUSE_CATEGORY_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(ABUSE_CATEGORY_NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(ABUSE_CATEGORY_DESCRIPTION));

        Mockito.verify(abuseCategoryService, Mockito.times(1)).findAll();
    }

    @Test
    public void shouldReturnEmptyList_whenNoCategories() throws Exception {
        // GIVEN
        List<AbuseCategory> emptyList = List.of();
        Mockito.when(abuseCategoryService.findAll()).thenReturn(emptyList);

        // WHEN
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URI_TEMPLATE));

        // THEN
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));

        Mockito.verify(abuseCategoryService, Mockito.times(1)).findAll();
    }
}