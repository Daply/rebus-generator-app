package com.rebusgenerator.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rebusgenerator.component.FileProcessor;
import com.rebusgenerator.component.RebusProcessor;
import com.rebusgenerator.dto.KeyWordImage;
import com.rebusgenerator.entity.Rebus;
import com.rebusgenerator.exception.FileLoadingException;
import com.rebusgenerator.service.LanguageService;
import com.rebusgenerator.service.RebusImagePuzzleService;
import com.rebusgenerator.service.RebusService;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class RebusController {
	
	/**
	 * Logger
	 */
	private final static org.apache.log4j.Logger LOGGER = Logger.getLogger(RebusController.class);
	
	@Autowired
	private RebusService rebusService;

	@Autowired
	private LanguageService languageService;
	
	@Autowired
	private RebusImagePuzzleService rebusImagePuzzleService;
	
	@Autowired
	private FileProcessor fileProcessor;
	
	@Autowired
	private RebusProcessor rebusProcessor;
	
	/**
	 * Get possible languages for creating rebus
	 * 
	 * @return list of languages abbreviations
	 */
	@RequestMapping(value = "/langs",
			method = RequestMethod.GET,
			produces = "application/json")
	public Iterable<String> getAllLanguages() {
		LOGGER.info("Getting all possible languages...");
		return languageService.getAllPossbleLanguages();
	}
	
	/**
	 * Get all possibe words for creating rebus
	 * @param lang - specified language for words
	 * @return list of words
	 */
	@RequestMapping(value = "/all/words",
			method = RequestMethod.GET,
			produces = "application/json")
	public Iterable<String> getAllWordsOfSpecifiedLang(@RequestParam("lang") final String lang) {
		LOGGER.info("Getting all words of specified language...");
		return rebusImagePuzzleService.getAllWordsOfSpecifiedLang(lang);
	}
	
	/**
	 * Get rebus sequence or specified word
	 * 
	 * @param word
	 * @return rebus
	 */
	@RequestMapping(value = "/rebus/sequence",
			method = RequestMethod.GET,
			produces = "application/json")
	public Rebus getRebusIfExists(@RequestParam("word") final String word) {
		LOGGER.info("Getting existing rebus...");
		return rebusService.getRebus(word);
	}
	
	/**
	 * Generate rebus with specified word and of specified language
	 * 
	 * @param word - specified word
	 * @param lang - specified language
	 * @return rebus sequence
	 * @throws IOException 
	 */
	@RequestMapping(value = "/rebus",
            method = RequestMethod.GET,
            produces = "application/json")
    public Map<String, String> generateRebus(@RequestParam("word") final String word, 
    		@RequestParam("lang") final String lang) throws IOException {
		LOGGER.info("Generating rebus query...");
		Map<String, String> jsonMap = new HashMap<>();
		try {
			String imagePath = rebusProcessor.generateRebusImage(word, lang);
			File file = new File(imagePath);
			String fileName = "";
			if (file.exists()) {
				fileName = file.getName();
			}
			else {
				throw new Exception("File hadn't been saved");
			}
		    String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.toPath()));
		    jsonMap.put("image_name", fileName);
		    jsonMap.put("content", encodeImage);
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("image_name", "error");
			jsonMap.put("content", "");
		}
        return jsonMap;
    }

	/**
	 * Upload pair by user for review to add
	 * this image in future to storage: word, it's image
	 * for adding to database of used images for creating rebuses
	 * 
	 * @param keyWordImage - model, represents word, language and picture file
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/review/image",
            method = RequestMethod.POST,
            produces = "application/json")
    public boolean uploadNewWordImageForReview(@ModelAttribute KeyWordImage keyWordImage) throws IOException {
		LOGGER.info("Uploading new word image for future review...");
		try {
			boolean uploaded = fileProcessor.saveUploadedFileForReview(keyWordImage.getWord(), keyWordImage.getLang(), keyWordImage.getFile());
			return uploaded;
		} catch (FileLoadingException e) {
			e.printStackTrace();
		}
		return false;
    }
	
	/**
	 * Upload pair by admin straight to storage: word, it's image
	 * for adding to database of used images for creating rebuses
	 * 
	 * @param keyWordImage - model, represents word, language and picture file
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/new/image",
            method = RequestMethod.POST,
            produces = "application/json")
    public boolean uploadNewWordImage(@ModelAttribute KeyWordImage keyWordImage) throws IOException {
		LOGGER.info("Uploading new word image...");

		try {
			fileProcessor.uploadFile(keyWordImage.getWord(), keyWordImage.getLang(), keyWordImage.getFile());
			rebusProcessor.saveNewWordAndItsAssociatedImage(keyWordImage.getWord(), keyWordImage.getLang());
			return true;
		} catch (FileLoadingException e) {
			e.printStackTrace();
		}
		return false;
    }
}
