package com.m42hub.m42hub_api.services.abuse;

import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;
import com.m42hub.m42hub_api.abuse.repository.AbuseCategoryRepository;
import com.m42hub.m42hub_api.abuse.service.AbuseCategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AbuseCategoryServiceTest {

    private static final Long SPAM_CATEGORY_ID = 1L;
    private static final String SPAM_CATEGORY_NAME = "Spam";
    private static final String SPAM_CATEGORY_DESC = "Conteúdo indesejado ou repetitivo";

    private static final Long ABUSE_CATEGORY_ID = 2L;
    private static final String ABUSE_CATEGORY_NAME = "Abuso";
    private static final String ABUSE_CATEGORY_DESC = "Linguagem ofensiva ou comportamento abusivo";

    @Mock
    private AbuseCategoryRepository abuseCategoryRepository;

    @InjectMocks
    private AbuseCategoryService abuseCategoryService;

    private AutoCloseable mocks;

    private AbuseCategory spamCategory;
    private AbuseCategory abuseCategory;

    @BeforeEach
    public void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        
        spamCategory = AbuseCategory.builder()
                .id(SPAM_CATEGORY_ID)
                .name(SPAM_CATEGORY_NAME)
                .description(SPAM_CATEGORY_DESC)
                .build();
                
        abuseCategory = AbuseCategory.builder()
                .id(ABUSE_CATEGORY_ID)
                .name(ABUSE_CATEGORY_NAME)
                .description(ABUSE_CATEGORY_DESC)
                .build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    public void shouldReturnAllCategories_whenFindAllIsCalled() {
        // GIVEN
        List<AbuseCategory> categories = List.of(spamCategory, abuseCategory);
        Mockito.when(abuseCategoryRepository.findAll()).thenReturn(categories);

        // WHEN
        List<AbuseCategory> result = abuseCategoryService.findAll();

        // THEN
        assertThat(result)
                .hasSize(2)
                .containsExactlyInAnyOrder(spamCategory, abuseCategory);
        Mockito.verify(abuseCategoryRepository, Mockito.times(1)).findAll();
    }
}