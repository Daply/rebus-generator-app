package com.rebusgenerator.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.rebusgenerator.entity.Language;
import com.rebusgenerator.repository.LanguageRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LangugeServiceIntegrationTest {
	
	@Autowired
    private LanguageService languageService;
	
	@MockBean
	private LanguageRepository languageRepository;
	
	@Before
    public void setUp() {
		Language lang1 = new Language("en");
		Language lang2 = new Language("es");
		Language lang3 = new Language("de");
		Language lang4 = new Language("ru");
		
		List<Language> expectedLangs = Arrays.asList(lang1, lang2, lang3, lang4);
		List<String> expectedLangsAbbr = Arrays.asList("en", "es", "de", "ru");

        //
        Mockito.when(languageRepository.findAll()).thenReturn(expectedLangs);
        Mockito.when(languageRepository.findAllLanguages()).thenReturn(expectedLangsAbbr);
    }
	
	@Test
    public void whenFindRebusByRebusWord_thenRebusMustBeReturned() {
		List<String> found = languageService.getAllPossbleLanguages();
		List<String> expected = Arrays.asList("en", "es", "de", "ru");
        assertThat(found, is(expected));
    }
}
