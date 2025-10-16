package com.m42hub.m42hub_api.services.profanity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m42hub.m42hub_api.abuse.entity.BannedWord;
import com.m42hub.m42hub_api.abuse.repository.BannedWordRepository;
import com.m42hub.m42hub_api.abuse.repository.UserFlagRepository;
import com.m42hub.m42hub_api.abuse.service.ProfanityService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ProfanityServiceTest {

    record TestRawRequestContent(String payload) {}

    private static final Long TEST_USER_ID = 1L;

    private static final String BAD_WORD_1 = "curse";
    private static final String BAD_WORD_2 = "xingamento";

    private static final String FIELD = "field-github-ou-algo-assim-na-request";
    private static final String ACTION = "PATCH";

    @Mock
    private BannedWordRepository bannedWordRepository;

    @Mock
    private UserFlagRepository userFlagRepository;

    @InjectMocks
    private ProfanityService profanityService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        List<BannedWord> bannedWords = List.of(
                new BannedWord(UUID.randomUUID(), BAD_WORD_1, true),
                new BannedWord(UUID.randomUUID(), BAD_WORD_2, true)
        );

        Mockito.when(bannedWordRepository.findByActiveTrue()).thenReturn(bannedWords);
        profanityService.reloadForbiddenWords();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void shouldPassForCleanText() throws JsonProcessingException {
        // GIVEN
        TestRawRequestContent requestContent = new TestRawRequestContent("Este é um texto normal");
        ObjectMapper mapper = new ObjectMapper();
        String text = mapper.writeValueAsString(requestContent);

        // WHEN
        Throwable thrown = Assertions.catchThrowable(() ->
                profanityService.validate(text, TEST_USER_ID, FIELD, ACTION)
        );

        // THEN
        Assertions.assertThat(thrown).isNull();
    }

    @Test
    void shouldThrowForForbiddenWord() throws JsonProcessingException  {
        // GIVEN
        TestRawRequestContent requestContent = new TestRawRequestContent("Esse texto tem curse dentro");
        ObjectMapper mapper = new ObjectMapper();
        String text = mapper.writeValueAsString(requestContent);

        // WHEN
        Throwable thrown = Assertions.catchThrowable(() ->
                profanityService.validate(text, TEST_USER_ID, FIELD, ACTION)
        );

        // THEN
        Assertions.assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        ResponseStatusException ex = (ResponseStatusException) thrown;
        Assertions.assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Mockito.verify(userFlagRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void shouldDetectMultipleForbiddenWords() throws JsonProcessingException {
        // GIVEN
        TestRawRequestContent requestContent = new TestRawRequestContent("curse e xingamento no mesmo texto");
        ObjectMapper mapper = new ObjectMapper();
        String text = mapper.writeValueAsString(requestContent);

        // WHEN
        Throwable thrown = Assertions.catchThrowable(() ->
                profanityService.validate(text, TEST_USER_ID, FIELD, ACTION)
        );

        // THEN
        Assertions.assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        Mockito.verify(userFlagRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void shouldDetectAttemptOfBypassForForbiddenWords() throws JsonProcessingException  {
        // GIVEN
        TestRawRequestContent requestContent1 = new TestRawRequestContent( "c\\u-r 5e no texto");
        ObjectMapper mapper1 = new ObjectMapper();
        String text1 = mapper1.writeValueAsString(requestContent1);

        TestRawRequestContent requestContent2 = new TestRawRequestContent( "x1n9 @m3nt0 no texto");
        ObjectMapper mapper2 = new ObjectMapper();
        String text2 = mapper2.writeValueAsString(requestContent2);

        // WHEN
        List<BannedWord> bannedWords = Arrays.asList(
                BannedWord.builder().word(BAD_WORD_1).active(true).build(),
                BannedWord.builder().word(BAD_WORD_2).active(true).build()
        );
        Mockito.when(bannedWordRepository.findByActiveTrue()).thenReturn(bannedWords);

        Throwable thrown1 = Assertions.catchThrowable(() ->
                profanityService.validate(text1, TEST_USER_ID, FIELD, ACTION)
        );

        Throwable thrown2 = Assertions.catchThrowable(() ->
                profanityService.validate(text2, TEST_USER_ID, FIELD, ACTION)
        );

        // THEN
        Assertions.assertThat(thrown1).isInstanceOf(ResponseStatusException.class);
        Assertions.assertThat(thrown2).isInstanceOf(ResponseStatusException.class);
        Mockito.verify(userFlagRepository, Mockito.times(2)).save(Mockito.any());
    }

    @Test
    void shouldIgnoreNullOrEmptyText() throws JsonProcessingException  {
        // GIVEN
        TestRawRequestContent requestContent1 = new TestRawRequestContent( null);
        ObjectMapper mapper1 = new ObjectMapper();
        String text1 = mapper1.writeValueAsString(requestContent1);

        TestRawRequestContent requestContent2 = new TestRawRequestContent( " ");
        ObjectMapper mapper2 = new ObjectMapper();
        String text2 = mapper2.writeValueAsString(requestContent2);

        // WHEN
        Throwable thrown1 = Assertions.catchThrowable(() ->
                profanityService.validate(text1, TEST_USER_ID, FIELD, ACTION)
        );
        Throwable thrown2 = Assertions.catchThrowable(() ->
                profanityService.validate(text2, TEST_USER_ID, FIELD, ACTION)
        );

        // THEN
        Assertions.assertThat(thrown1).isNull();
        Assertions.assertThat(thrown2).isNull();
    }
}