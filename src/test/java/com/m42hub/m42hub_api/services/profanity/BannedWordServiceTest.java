package com.m42hub.m42hub_api.services.profanity;

import com.m42hub.m42hub_api.abuse.entity.BannedWord;
import com.m42hub.m42hub_api.abuse.repository.BannedWordRepository;
import com.m42hub.m42hub_api.abuse.service.BannedWordService;
import com.m42hub.m42hub_api.abuse.service.ProfanityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class BannedWordServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(BannedWordServiceTest.class);

    private static final String BAD_WORD_1 = "curse";
    private static final String BAD_WORD_2 = "xingamento";

    @Mock
    private BannedWordRepository bannedWordRepository;

    @Mock
    private ProfanityService profanityService;

    @InjectMocks
    private BannedWordService bannedWordService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void shouldReturnAllBannedWordsSortedAsc() {
        // GIVEN
        BannedWord bannedWord1 = BannedWord.builder()
                .id(UUID.randomUUID())
                .word(BAD_WORD_1)
                .isActive(false)
                .build();

        BannedWord bannedWord2 = BannedWord.builder()
                .id(UUID.randomUUID())
                .word(BAD_WORD_2)
                .isActive(false)
                .build();

        // WHEN
        Mockito.when(bannedWordRepository.findAllByOrderByWordAsc()).thenReturn(List.of(bannedWord1, bannedWord2));
        Mockito.when(profanityService.normalizeAndCompressWord(BAD_WORD_1)).thenReturn(BAD_WORD_1);
        Mockito.when(profanityService.normalizeAndCompressWord(BAD_WORD_2)).thenReturn(BAD_WORD_2);
        List<BannedWord> foundSortedWords = bannedWordService.getBannedWords();

        // THEN
        Assertions.assertEquals(2, foundSortedWords.size());
        Assertions.assertEquals(bannedWord1, foundSortedWords.getFirst());
        Assertions.assertEquals(bannedWord2, foundSortedWords.getLast());
    }

    @Test
    void shouldProcessAndSaveAllBannedWordsOfAGivenList() {
        // GIVEN
        List<String> wordsList = List.of(BAD_WORD_1, BAD_WORD_2);

        BannedWord bannedWord1 = BannedWord.builder()
                .id(UUID.randomUUID())
                .word(BAD_WORD_1)
                .isActive(false)
                .build();

        BannedWord bannedWord2 = BannedWord.builder()
                .id(UUID.randomUUID())
                .word(BAD_WORD_2)
                .isActive(false)
                .build();

        // WHEN
        Mockito.when(bannedWordRepository.findAllByOrderByWordAsc()).thenReturn(List.of(bannedWord1, bannedWord2));
        // Nesse caso é BAD_WORD == BAD_WORD_NORMALIZED (não está "censurado")
        Mockito.when(profanityService.normalizeAndCompressWord(BAD_WORD_1)).thenReturn(BAD_WORD_1);
        Mockito.when(profanityService.normalizeAndCompressWord(BAD_WORD_2)).thenReturn(BAD_WORD_2);
        boolean isSaved = bannedWordService.processBannedWords(wordsList);

        // THEN
        Assertions.assertTrue(isSaved);
        Mockito.verify(bannedWordRepository, Mockito.times(1)).findAllByOrderByWordAsc();
        Mockito.verify(bannedWordRepository, Mockito.times(1)).saveAll(List.of(bannedWord1, bannedWord2));
        Mockito.verify(profanityService, Mockito.times(2)).normalizeAndCompressWord(BAD_WORD_1);
        Mockito.verify(profanityService, Mockito.times(2)).normalizeAndCompressWord(BAD_WORD_2);
    }

    @Test
    void shouldUpdateBannedWordsStatus_WhenExistingWordIsGiven() {
        // GIVEN
        BannedWord bannedWord = new BannedWord(UUID.randomUUID(), BAD_WORD_1, true);

        // WHEN
        Mockito.when(bannedWordRepository.findAllByOrderByWordAsc()).thenReturn(List.of(bannedWord));
        Mockito.when(profanityService.normalizeAndCompressWord(BAD_WORD_1)).thenReturn(BAD_WORD_1);
        boolean isUpdated = bannedWordService.updateBannedWordStatus(bannedWord.getWord(), bannedWord.isActive());

        // THEN
        Assertions.assertTrue(isUpdated);
        Mockito.verify(bannedWordRepository, Mockito.times(1)).findAllByOrderByWordAsc();
        Mockito.verify(bannedWordRepository, Mockito.times(1)).save(bannedWord);
        Mockito.verify(profanityService, Mockito.times(2)).normalizeAndCompressWord(BAD_WORD_1);
    }
}