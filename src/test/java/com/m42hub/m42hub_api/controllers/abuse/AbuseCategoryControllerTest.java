package com.m42hub.m42hub_api.controllers.abuse;

import com.m42hub.m42hub_api.abuse.controller.AbuseCategoryController;
import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;
import com.m42hub.m42hub_api.abuse.service.AbuseCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AbuseCategoryController.class)
public class AbuseCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AbuseCategoryService abuseCategoryService;

    private List<AbuseCategory> categories;

    @BeforeEach
    public void setUp() {
        AbuseCategory spam = AbuseCategory.builder()
                .id(1L)
                .name("Spam")
                .description("Conteúdo indesejado ou repetitivo")
                .build();

        AbuseCategory abuse = AbuseCategory.builder()
                .id(2L)
                .name("Abuso")
                .description("Linguagem ofensiva ou comportamento abusivo")
                .build();

        categories = List.of(spam, abuse);
    }

    @Test
    public void shouldReturnAllCategories_whenGetAllCategoriesIsCalled() throws Exception {
        // GIVEN
        when(abuseCategoryService.findAll()).thenReturn(categories);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/abuse/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Spam"))
                .andExpect(jsonPath("$[0].description").value("Conteúdo indesejado ou repetitivo"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Abuso"))
                .andExpect(jsonPath("$[1].description").value("Linguagem ofensiva ou comportamento abusivo"));
    }
}