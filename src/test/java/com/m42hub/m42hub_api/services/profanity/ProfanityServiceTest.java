package com.m42hub.m42hub_api.services.profanity;

import com.m42hub.m42hub_api.abuse.entity.BannedWord;
import com.m42hub.m42hub_api.abuse.repository.BannedWordRepository;
import com.m42hub.m42hub_api.abuse.service.ProfanityService;
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

import java.util.*;

public class ProfanityServiceTest {
    private final Logger logger = LoggerFactory.getLogger(ProfanityServiceTest.class);

    private static final String BAD_WORD_1 = "curse";
    private static final String BAD_WORD_2 = "xingamento";

    @Mock
    private BannedWordRepository bannedWordRepository;

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
        // É chamado no construtor da classe real de testes pela função: reloadForbiddenWords()
        Mockito.when(bannedWordRepository.findByIsActiveTrue()).thenReturn(bannedWords);
        profanityService.reloadForbiddenWords();
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void shouldNormalizeAndCompressGivingInputWord() {
        // GIVEN
        String notNormalizedText = "xing@m3nt0";

        // WHEN
        String normalizedText = profanityService.normalizeAndCompressWord(notNormalizedText);

        // THEN
        Assertions.assertThat(normalizedText).isEqualTo(BAD_WORD_2);
    }

    @Test
    void shouldRemoveRepeatedCharacters() {
        // GIVEN
        String input = "baaaaannnnn";
        String expected = "ban";

        // WHEN
        String result = profanityService.normalizeAndCompressWord(input);

        // THEN
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldNormalizeSpecialCharacters() {
        // GIVEN
        String input = "h@ck!n9";
        String expected = "hacking";

        // WHEN
        String result = profanityService.normalizeAndCompressWord(input);

        // THEN
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldHandleMixedCase() {
        // GIVEN
        String input = "TeStE";
        String expected = "teste";

        // WHEN
        String result = profanityService.normalizeAndCompressWord(input);

        // THEN
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldHandleNullInput() {
        // GIVEN
        String input = null;
        String expected = "";

        // WHEN
        String result = profanityService.normalizeAndCompressWord(input);

        // THEN
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldHandleEmptyInput() {
        // GIVEN
        String input = "";
        String expected = "";

        // WHEN
        String result = profanityService.normalizeAndCompressWord(input);

        // THEN
        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldAddWordIntoInternalCache() {
        // GIVEN
        String word = "Hello";
        String normalizedWord = profanityService.normalizeAndCompressWord(word);

        // WHEN
        profanityService.addToCache(word);
        Set<String> words = profanityService.getForbiddenWords();

        // THEN
        Assertions.assertThat(words).contains(normalizedWord);
    }

    @Test
    void shouldRemoveWordFromInternalCache() {
        // GIVEN
        String word = "Hello";
        String normalizedWord = profanityService.normalizeAndCompressWord(word);

        // WHEN
        profanityService.removeFromCache(word);
        Set<String> words = profanityService.getForbiddenWords();

        // THEN
        Assertions.assertThat(words).doesNotContain(normalizedWord);
    }
}