package com.m42hub.m42hub_api.services.profanity;

import com.m42hub.m42hub_api.profanity.entity.BannedWord;
import com.m42hub.m42hub_api.profanity.repository.BannedWordRepository;
import com.m42hub.m42hub_api.profanity.repository.WordFlagRepository;
import com.m42hub.m42hub_api.profanity.service.ProfanityService;
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

    private static final UUID TEST_USER_ID = UUID.randomUUID();

    private static final String BAD_WORD_1 = "curse";
    private static final String BAD_WORD_2 = "xingamento";

    private static final String FIELD = "field";
    private static final String ACTION = "create";

    @Mock
    private BannedWordRepository bannedWordRepository;

    @Mock
    private WordFlagRepository wordFlagRepository;

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
    void shouldPassForCleanText() {
        // GIVEN
        String text = "Este é um texto normal";

        // WHEN
        Throwable thrown = Assertions.catchThrowable(() ->
                profanityService.validate(text, TEST_USER_ID, FIELD, ACTION)
        );

        // THEN
        Assertions.assertThat(thrown).isNull();
    }

    @Test
    void shouldThrowForForbiddenWord() {
        // GIVEN
        String text = "Esse texto tem curse dentro";

        // WHEN
        Throwable thrown = Assertions.catchThrowable(() ->
                profanityService.validate(text, TEST_USER_ID, FIELD, ACTION)
        );

        // THEN
        Assertions.assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        ResponseStatusException ex = (ResponseStatusException) thrown;
        Assertions.assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Mockito.verify(wordFlagRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void shouldDetectMultipleForbiddenWords() {
        // GIVEN
        String text = "curse e xingamento no mesmo texto";

        // WHEN
        Throwable thrown = Assertions.catchThrowable(() ->
                profanityService.validate(text, TEST_USER_ID, FIELD, ACTION)
        );

        // THEN
        Assertions.assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        Mockito.verify(wordFlagRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void shouldDetectAttemptOfBypassForForbiddenWords() {
        // GIVEN
        String text1 = "c\\u-r 5e no texto";

        String text2 = "x1n9 @m3nt0 no texto";

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
        Mockito.verify(wordFlagRepository, Mockito.times(2)).save(Mockito.any());
    }

    @Test
    void shouldIgnoreNullOrEmptyText() {
        // GIVEN
        String text1 = null;
        String text2 = "  ";

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