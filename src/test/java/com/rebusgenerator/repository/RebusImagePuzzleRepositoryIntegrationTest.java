package com.rebusgenerator.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rebusgenerator.entity.ImageWordType;
import com.rebusgenerator.entity.Language;
import com.rebusgenerator.entity.RebusImagePuzzle;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RebusImagePuzzleRepositoryIntegrationTest {
 
	@Autowired
	RebusImagePuzzleRepository rebusImagePuzzleRepository;
	
	@Autowired
    private LanguageRepository languageRepository;
    
    @Test
    public void whenFindAllTheMostSimilarWords_thenReturnListOfWords() {
    	Language lang = languageRepository.findLanguageByLangAbbr("en");
    	RebusImagePuzzle rebusImagePuzzle = new RebusImagePuzzle("factorial", "factorial_en.png");
    	rebusImagePuzzle.setImageWordType(ImageWordType.WORD);
    	rebusImagePuzzle.setWordLang(lang);
        
        rebusImagePuzzleRepository.save(rebusImagePuzzle);

        List<String> actual = rebusImagePuzzleRepository
        		.findAllTheMostSimilarWords("fact", "en");
        
        rebusImagePuzzleRepository.delete(rebusImagePuzzle);
 
        assertTrue(actual.contains("factorial"));
    }
    
    @Test
    public void whenFindImageByWordAndLang_thenReturnImageName() {
    	Language lang = languageRepository.findLanguageByLangAbbr("en");
    	RebusImagePuzzle rebusImagePuzzle = new RebusImagePuzzle("dfkfnkjf", "dfkfnkjf_en.png");
    	rebusImagePuzzle.setImageWordType(ImageWordType.LETTER);
    	rebusImagePuzzle.setWordLang(lang);
    	String expected = "dfkfnkjf_en.png";

    	rebusImagePuzzleRepository.save(rebusImagePuzzle);

        String actual = rebusImagePuzzleRepository
        		.findImageByWordAndLang("dfkfnkjf", "en");
        
        rebusImagePuzzleRepository.delete(rebusImagePuzzle);
 
        assertThat(actual, is(expected));
    }
}
