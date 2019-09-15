package com.rebusgenerator.component;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rebusgenerator.algorithm.ImageProcessor;
import com.rebusgenerator.algorithm.RebusSequenceGenerator;
import com.rebusgenerator.algorithm.RebusSequenceParser;
import com.rebusgenerator.entity.Rebus;
import com.rebusgenerator.entity.RebusImagePuzzle;
import com.rebusgenerator.entity.Syllable;
import com.rebusgenerator.exception.FileLoadingException;
import com.rebusgenerator.service.RebusImagePuzzleService;
import com.rebusgenerator.service.RebusService;
import com.rebusgenerator.service.SyllableService;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Component
public class RebusProcessor {
	
	/**
	 * Logger
	 */
	private final static org.apache.log4j.Logger LOGGER = Logger.getLogger(RebusProcessor.class);
	
	@Autowired
	RebusImagePuzzleService rebusImagePuzzleService;
	
	@Autowired
	RebusService rebusService;
	
	@Autowired
	SyllableService syllableService;
	
	@Autowired
	RebusSequenceGenerator rebusSequenceGenerator;
	
	@Autowired 
	RebusSequenceParser rebusSequenceParser;
	
	@Autowired
	ImageProcessor imageProcessor;
	
	@Autowired
	FileProcessor fileProcessingService;
	
	public void saveNewWordAndItsAssociatedImage(String word, String lang) {
		rebusImagePuzzleService.saveWordAndItsAssociatedImage(word, lang);
    }
	
	/**
	 * Get all words from datatbase.
	 * 
	 * @param lang - word language
	 * @return list of the all words from database
	 */
	public List<String> getAllWords(String lang) {
		return rebusImagePuzzleService.getAllWordsOfSpecifiedLang(lang);
    }
	
	/**
	 * Get the most similar words from database to the word, 
	 * from what needed to create a rebus
	 * 
	 * @param word - the word, from what needed to create a rebus
	 * @param lang - word language
	 * @return list of the most similar words from database
	 */
	public Collection<String> getTheMostSimilarWords(String word, String lang) {
		Set<String> words = new HashSet<String>();
		for (int i = 0; i < word.length() - 1; i++) {
			// add from words using LIKE keyword
			// words.addAll(rebusImagePuzzleService.getAllTheMostSimilarWords(word.substring(i, i+2), lang));
			// add from Syllable
			Syllable foundSyllable = syllableService.findSyllable(word.substring(i, i+2));
			if (foundSyllable != null) {
				Set<RebusImagePuzzle> set = foundSyllable.getConnectedWords();
				for (RebusImagePuzzle r: set) {
					words.add(r.getImageWord());
				}
			}
		}
		return words;
    }
	
	/**
	 * Generate a rebus sequence for a word
	 * 
	 * @param word - the word, from what needed to create a rebus
	 * @param lang - word language
	 * @return list of rebus sequence queries
	 * @throws Exception 
	 */
	public List<String> generateRebusSequence(String word, String lang) throws Exception {
		Collection<String> databaseWords = getTheMostSimilarWords(word, lang);
		if (databaseWords.isEmpty()) {
			databaseWords = getAllWords(lang);
		}
		return rebusSequenceGenerator.generateRebus(word, databaseWords);
    }
	
	/**
	 * Loads all needed images for rebus
	 * 
	 * @param word - the word, from what needed to create a rebus
	 * @param lang - word language
	 * @param images - list of images names
	 * @return temporary directory, where images stored
	 * @throws IOException 
	 * @throws FileLoadingException 
	 */
	public String downloadAllImagesForRebus(String word, String lang, List<String> images) throws IOException, FileLoadingException {
		String folderName = word + "_" + lang;
		return fileProcessingService.downloadFiles(images, folderName);
    }
	
	/**
	 * Generate the final rebus image 
	 * 
	 * @param word - the word, from what needed to create a rebus
	 * @param lang - word language
	 * @return result image path
	 * @throws Exception 
	 */
	public String generateRebusImage(String word, String lang) throws Exception {
		LOGGER.info("Processing rebus generating...");
		// save rebus as word and rebus sequence for future use
		List<String> rebusSequence = null;
		// chack if such rebus already exists in database
		Rebus existingRebus = rebusService.getRebus(word);
		if (existingRebus != null) {
			// get existing rebus sequence
			rebusSequence = existingRebus.getRebusSequence();
		}
		else {
			rebusSequence = generateRebusSequence(word, lang);
			// add rebus to database
			Rebus newRebus = new Rebus();
			newRebus.setRebusWord(word);
			newRebus.setRebusSequence(rebusSequence);
			rebusService.save(newRebus);
		}
		List<List<List<String>>> wordsImagesGroups = rebusSequenceParser
					    .parseRebusSequence(rebusSequence, lang);
		// load all images from storage
		String imagesDirectory = downloadAllImagesForRebus(word, lang, rebusSequenceParser.getAllImagesNeededForRebus());
		// image processing	
		String rebusDirectory = imageProcessor.mergeAllWordsImagesGroups(word, lang, wordsImagesGroups, imagesDirectory);
		
		LOGGER.info("Rebus generating process finished successfully");
		
		// return directory on server were this image is
		return rebusDirectory;
    }
	
}
