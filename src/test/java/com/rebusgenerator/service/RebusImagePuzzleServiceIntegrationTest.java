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

import com.rebusgenerator.entity.ImageWordType;
import com.rebusgenerator.entity.Language;
import com.rebusgenerator.entity.RebusImagePuzzle;
import com.rebusgenerator.repository.RebusImagePuzzleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RebusImagePuzzleServiceIntegrationTest {

	@Autowired
    private RebusImagePuzzleService rebusImagePuzzleService;
	
	@MockBean
	private RebusImagePuzzleRepository rebusImagePuzzleRepository;
	
	@Before
    public void setUp() {
		Language lang = new Language("en");
		RebusImagePuzzle rebusImagePuzzle1 = new RebusImagePuzzle();
		rebusImagePuzzle1.setRebusImagePuzzleId(1l);
    	rebusImagePuzzle1.setImageWord("dog");
    	rebusImagePuzzle1.setImageWordType(ImageWordType.WORD);
    	rebusImagePuzzle1.setWordLang(lang);
    	rebusImagePuzzle1.setImageName("dog.png");
    	RebusImagePuzzle rebusImagePuzzle2 = new RebusImagePuzzle();
    	rebusImagePuzzle2.setRebusImagePuzzleId(2l);
    	rebusImagePuzzle2.setImageWord("hen");
    	rebusImagePuzzle2.setImageWordType(ImageWordType.WORD);
    	rebusImagePuzzle2.setWordLang(lang);
    	rebusImagePuzzle2.setImageName("hen.png");
    	RebusImagePuzzle rebusImagePuzzle3 = new RebusImagePuzzle();
    	rebusImagePuzzle3.setRebusImagePuzzleId(3l);
    	rebusImagePuzzle3.setImageWord("pen");
    	rebusImagePuzzle3.setImageWordType(ImageWordType.WORD);
    	rebusImagePuzzle3.setWordLang(lang);
    	rebusImagePuzzle3.setImageName("pen.png");

    	List<String> allWordsOfLang = Arrays.asList(rebusImagePuzzle1.getImageWord(),
        		rebusImagePuzzle2.getImageWord(), rebusImagePuzzle3.getImageWord());
    	
    	String word = "en";
    	List<String> theMostSimilar = Arrays.asList(rebusImagePuzzle2.getImageWord(), rebusImagePuzzle3.getImageWord());

        //
        Mockito.when(rebusImagePuzzleRepository
        		.findImageByWordAndLang(rebusImagePuzzle1.getImageWord(), 
        				rebusImagePuzzle1.getWordLang().getLang())).thenReturn(rebusImagePuzzle1.getImageName());
        Mockito.when(rebusImagePuzzleRepository
        		.findImageByWordAndLang(rebusImagePuzzle2.getImageWord(), 
        				rebusImagePuzzle2.getWordLang().getLang())).thenReturn(rebusImagePuzzle2.getImageName());
        Mockito.when(rebusImagePuzzleRepository
        		.findImageByWordAndLang(rebusImagePuzzle3.getImageWord(), 
        				rebusImagePuzzle3.getWordLang().getLang())).thenReturn(rebusImagePuzzle3.getImageName());
        //
        Mockito.when(rebusImagePuzzleRepository
        		.findAllWordsByLang(lang.getLang())).thenReturn(allWordsOfLang);
        
        //
        Mockito.when(rebusImagePuzzleRepository
        		.findAllTheMostSimilarWords(word, lang.getLang())).thenReturn(theMostSimilar);
    }
	
	@Test
    public void whenGetAllWordsOfSpecifiedLang_thenAllWordsOfSpecifiedLanguageMustBeReturned() {
        String lang = "en";
        List<String> found = rebusImagePuzzleService.getAllWordsOfSpecifiedLang(lang);
        List<String> expected = Arrays.asList("dog", "hen", "pen");
        assertThat(found, is(expected));
    }
	
	@Test
    public void whenGetAllTheMostSimilarWords_thenAllSimilarWordsMustBeReturned() {
		String word = "en";
        String lang = "en";
        List<String> found = rebusImagePuzzleService.getAllTheMostSimilarWords(word, lang);
        List<String> expected = Arrays.asList("hen", "pen");
        assertThat(found, is(expected));
    }
	
	@Test
    public void whenGetImageBySpecifiedWordAndLanguage_thenImageNameMustBeReturned() {
		String word = "hen";
        String lang = "en";
        String found = rebusImagePuzzleService.getImageBySpecifiedWordAndLanguage(word, lang);
        String expected = "hen.png";
        assertThat(found, is(expected));
    }
	
}
