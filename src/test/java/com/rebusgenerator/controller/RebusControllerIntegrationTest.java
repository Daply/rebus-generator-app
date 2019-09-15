package com.rebusgenerator.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.rebusgenerator.component.FileProcessor;
import com.rebusgenerator.component.RebusProcessor;
import com.rebusgenerator.entity.Rebus;
import com.rebusgenerator.service.LanguageService;
import com.rebusgenerator.service.RebusImagePuzzleService;
import com.rebusgenerator.service.RebusService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(RebusController.class)
public class RebusControllerIntegrationTest {
	
	@Value("${images.file.folder}")
	private String IMAGES_FOLDER;
	 
    @Autowired
    private MockMvc mvc;
 
    @MockBean
    private RebusService rebusService;
    
    @MockBean
    private LanguageService languageService;
    
    @MockBean
    private RebusImagePuzzleService rebusImagePuzzleService;
    
    @MockBean
    private FileProcessor fileProcessingService;
	
    @MockBean
    private RebusProcessor rebusGeneratorService;
    
    @MockBean
    private UserDetailsService userDetailsService;
 
    @Test
    public void givenImageWords_whenGetAllWords_thenReturnJsonArray()
      throws Exception {
        List<String> allWords = Arrays.asList("phone", "pen", "ginger", "dog", "cat");
        given(rebusImagePuzzleService.getAllWordsOfSpecifiedLang("en")).willReturn(allWords);
     
        mvc.perform(get("/api/all/words?lang=en")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0]", is("phone")))
          .andExpect(jsonPath("$[1]", is("pen")))
          .andExpect(jsonPath("$[2]", is("ginger")))
          .andExpect(jsonPath("$[3]", is("dog")))
          .andExpect(jsonPath("$[4]", is("cat")));
        verify(rebusImagePuzzleService, VerificationModeFactory.times(1)).getAllWordsOfSpecifiedLang("en");
        reset(rebusImagePuzzleService);
    }
    
    @Test
    public void givenRebus_whenGetRebus_thenReturnJsonRebus()
      throws Exception {
        Rebus rebus = new Rebus();
        rebus.setRebusWord("phenomena");
        given(rebusService.getRebus("phenomena")).willReturn(rebus);
     
        mvc.perform(get("/api/rebus/sequence?word=phenomena")
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.rebusWord", is("phenomena")));
        verify(rebusService, VerificationModeFactory.times(1)).getRebus("phenomena");
        reset(rebusService);
    }
    
    @Test
    public void givenWord_whenGetRebus_thenReturnJsonImageNameAndContent()
      throws Exception {
        String givenWord = "test";
        String lang = "en";
        String fileName = givenWord + ".png";
        String path = getResourcesPath() + fileName;
        createTestFile(path);
        given(rebusGeneratorService.generateRebusImage(givenWord, lang))
        							.willReturn(path);
     
        mvc.perform(get("/api/rebus?word=" + givenWord + "&lang=" + lang)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.image_name", is(fileName)));
        deleteTestFile(path);
        verify(rebusGeneratorService, VerificationModeFactory.times(1)).generateRebusImage(givenWord, lang);
        reset(rebusGeneratorService);
    }
    
    private void createTestFile(String path) throws IOException {
    	FileOutputStream out = new FileOutputStream(path);
        out.write("test".getBytes());
        out.close();
    }
    
    private void deleteTestFile(String path) throws IOException {
    	File file = new File(path);
        file.delete();
    }
    
    private String getResourcesPath() {
		return IMAGES_FOLDER;
	}
}
