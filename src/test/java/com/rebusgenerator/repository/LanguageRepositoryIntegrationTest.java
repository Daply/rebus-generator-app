package com.rebusgenerator.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rebusgenerator.entity.Language;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LanguageRepositoryIntegrationTest {
	
	@Autowired
    private LanguageRepository languageRepository;
	
	@Test
    public void whenFindLanguage_thenReturnLanguage() {
		Language expected = new Language("es");
		languageRepository.save(expected);
        Language actual = languageRepository.findLanguageByLangAbbr("es");
        languageRepository.delete(expected);
        assertThat(actual.getLang(), is(expected.getLang()));
    }
}
