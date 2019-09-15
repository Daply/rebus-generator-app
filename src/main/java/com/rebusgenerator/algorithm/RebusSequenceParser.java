package com.rebusgenerator.algorithm;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rebusgenerator.exception.ParserException;
import com.rebusgenerator.service.RebusImagePuzzleService;

/**
 * Parses rebus sequence and changes every
 * rebus query to image name
 * 
 * 
 *        word:            phenomena
 *      
 *  rebus parts:      phe     nome     na
 *   
 * similar words:    phone    note    note
 * 
 * rebus sequencce:  phone [delete o] [delete n] +
 *                   note [change t=m] + 
 *                   note [back delete o] [back delete t] [back delete e] [back insert e]
 *                   
 * phone [delete o] [delete n] - word group
 * [delete n] - word query
 * delete - query command
 * 
 * @author Pleshchankova Daria
 *
 */
@Component
public class RebusSequenceParser {
	
	/**
	 * Logger
	 */
	private final static org.apache.log4j.Logger LOGGER = Logger.getLogger(RebusSequenceParser.class);
	
	/**
	 * List of names of images, needed to create a rebus
	 */
	private List<String> allImagesNeededForRebus = null;
	
	@Autowired
	RebusImagePuzzleService rebusImagePuzzleService;
	
	@PostConstruct
	public void init() {
		this.allImagesNeededForRebus = new ArrayList<String>();
	}

	public List<String> getAllImagesNeededForRebus() {
		return allImagesNeededForRebus;
	}

	/**
	 * Parse all rebus sequence queries and make a table of
	 * appropriate image names 
	 * 
	 * @param rebusSequence - sequence of rebus queries
	 * @return list of images groups, how it must be combines to
	 *         result image
	 * @throws ParserException 
	 */
	public List<List<List<String>>> parseRebusSequence(List<String> rebusSequence, String lang) throws ParserException {	
		
		LOGGER.info("Parsing rebus sequence...");
		
		if (rebusSequence == null || lang == null) {
			LOGGER.error("Some of arguments are null!");
			if (rebusSequence == null && lang == null)
				throw new ParserException("all arguments are null");
			else if (rebusSequence == null)
				throw new ParserException("rebus sequence is null");
			else if (lang == null)
				throw new ParserException("lang is null");
		}
		
		List<List<List<String>>> imagesGroups = new ArrayList<List<List<String>>>();
		List<String> imagesOfOneGroup = new ArrayList<String>();
		for (String query: rebusSequence) {
			if (query == null)
				throw new ParserException("query in rebus sequence is null");
			if (query.equals("+")) {
				imagesGroups.add(processWordGroupByLayers(imagesOfOneGroup, lang));
				imagesOfOneGroup = new ArrayList<String>();
			}
			else {
				imagesOfOneGroup.add(query);
			}
		}
		imagesGroups.add(processWordGroupByLayers(imagesOfOneGroup, lang));
		
		LOGGER.info("Rebus sequence parsed successfully");
		
		return imagesGroups;
    }
	
	/**
	 * Divide group on two layers:
	 * 
	 * First one contains main word with front and back deletes
	 * Second contains all inserts, deletes or changes of letters in
	 * the middle of main word are needed  
	 * 
	 * @param queries - rebus queries
	 * @throws ParserException 
	 */
	public List<List<String>> processWordGroupByLayers(List<String> queriesOfWordGroup, String lang) throws ParserException {
		
		if (queriesOfWordGroup == null || lang == null) {
			LOGGER.error("Some of arguments are null!");
			if (queriesOfWordGroup == null && lang == null)
				throw new ParserException("all arguments are null");
			else if (queriesOfWordGroup == null)
				throw new ParserException("list queries of word group is null");
			else if (lang == null)
				throw new ParserException("lang is null");
		}
		
		List<List<String>> tableOfImages = new ArrayList<List<String>>();
		List<String> firstLayer = new ArrayList<String>();
		List<String> secondLayer = new ArrayList<String>();
		
		for (String query: queriesOfWordGroup) {
			if (query == null)
				throw new ParserException("query in list queries of word group is null");
			if (query.matches("\\[front .*") ||
					query.matches("\\[back .*") ||
					query.matches("[a-z]+")) {
				firstLayer.add(query);
			}
			else {
				secondLayer.add(query);
			}
		}
		
		if (!firstLayer.isEmpty()) {
			firstLayer = processQueryLayer(firstLayer, lang);
			tableOfImages.add(firstLayer);
		}
		if (!secondLayer.isEmpty()) {
			secondLayer = processQueryLayer(secondLayer, lang);
			tableOfImages.add(secondLayer);
		}
		
		return tableOfImages;
	}
	
	/**
	 * Process one layer of queries of one word group
	 * 
	 * @param layer - layer of images
	 * @return
	 * @throws ParserException 
	 */
	public List<String> processQueryLayer(List<String> layer, String lang) throws ParserException { 
		
		if (layer == null || lang == null) {
			LOGGER.error("Some of arguments are null!");
			if (layer == null && lang == null)
				throw new ParserException("all arguments are null");
			else if (layer == null)
				throw new ParserException("layer is null");
			else if (lang == null)
				throw new ParserException("lang is null");
		}
		
		List<String> imagesOfOneLayer = new ArrayList<>();
		for (String query: layer) {
			if (query == null)
				throw new ParserException("query in layer list is null");
			imagesOfOneLayer.addAll(processOneQuery(query, lang));
		}
		return imagesOfOneLayer;
    }
	
	/**
	 * Process one query of word group
	 * 
	 * @param query - rebus sequence query
	 * @return list of images
	 * @throws ParserException 
	 */
	public List<String> processOneQuery(String query, String lang) throws ParserException {
		
		if (query == null || lang == null) {
			LOGGER.error("Some of arguments are null!");
			if (query == null && lang == null)
				throw new ParserException("all arguments are null");
			else if (query == null)
				throw new ParserException("query is null");
			else if (lang == null)
				throw new ParserException("lang is null");
		}
		
		List<String> images = new ArrayList<>();
		if (query.contains("[") && query.contains("]")) {
	        String queryCommand = query.substring(query.indexOf("[")+1, query.indexOf("]"));
	        String queryData = query.substring(query.indexOf("]")+1, query.length()).trim();
	        String [] queryCommandParts = queryCommand.split("\\s");
	        if (queryCommandParts.length > 1) {
	        	images.addAll(processQueryCommand(queryCommandParts[0], 
	        			          queryData, queryCommandParts[1], lang));
	        }
		}
		else {
			if (rebusImagePuzzleService != null) {
				String imageName = rebusImagePuzzleService.getImageBySpecifiedWordAndLanguage(query, lang);
				if (imageName != null) {
					images.add(imageName);
				}
			}
		}
		if (this.allImagesNeededForRebus != null) {
			this.allImagesNeededForRebus.addAll(images);
		}
        return images;
	}
	
	/**
	 * Process one query command
	 * 
	 * @param wordSide - word side can be front, middle or back,
	 *        dependently from query
	 * @param queryData - if it is needed to delete some letter
	 *        or insert, the data is a letter
	 * @param command - query command (delete, changeor insert)
	 * @param lang - language
	 * @return image names needed to represent the query
	 * @throws ParserException
	 */
	public List<String> processQueryCommand(String wordSide, String queryData, String command, String lang) throws ParserException {
		
		if (wordSide == null || queryData == null ||
				command == null || lang == null) {
			LOGGER.error("Some of arguments are null!");
			if (wordSide == null && queryData == null &&
					command == null && lang == null)
				throw new ParserException("all arguments are null");
			else if (wordSide == null)
				throw new ParserException("word side is null");
			else if (queryData == null)
				throw new ParserException("query data is null");
			else if (command == null)
				throw new ParserException("command is null");
			else if (lang == null)
				throw new ParserException("lang is null");
		}
		
		List<String> imageNames = new ArrayList<String>();
		if (wordSide.contentEquals("front")) {
			if (command.contentEquals("delete")) {
				if (rebusImagePuzzleService != null) {
					String imageName = rebusImagePuzzleService
							.getImageBySpecifiedWordAndLanguage("front_comma", lang);
					if (imageName != null) {
						imageNames.add(imageName);
					}
				}
        	}
		}
		else if (wordSide.contentEquals("middle")) {
			if (command.contentEquals("delete")) {
				if (rebusImagePuzzleService != null) {
					String imageName = rebusImagePuzzleService
							.getImageBySpecifiedWordAndLanguage("crossed_" + queryData, lang);
					if (imageName != null) {
						imageNames.add(imageName);
					}
				}
        	}
		}
		else if (wordSide.contentEquals("back")) {
			if (command.contentEquals("delete")) {
				if (rebusImagePuzzleService != null) {
					String imageName = rebusImagePuzzleService
							.getImageBySpecifiedWordAndLanguage("back_comma", lang);
					if (imageName != null) {
						imageNames.add(imageName);
					}
				}
        	}
		}
		if (command.contentEquals("change") ||
				command.contentEquals("insert")) {
			String[] dataParts = queryData.split("=");
			if (dataParts.length > 1) {
				if (dataParts[0].matches("\\w") &&
						dataParts[1].matches("\\w") &&
						rebusImagePuzzleService != null) {
					String imageNameData = rebusImagePuzzleService
							.getImageBySpecifiedWordAndLanguage(dataParts[0], lang);
					if (imageNameData != null) {
						imageNames.add(imageNameData);
					}
					String imageNameEquals = rebusImagePuzzleService
							.getImageBySpecifiedWordAndLanguage("equals_sign", lang);
					if (imageNameEquals != null) {
						imageNames.add(imageNameEquals);
					}
					String imageNameData1 = rebusImagePuzzleService
							.getImageBySpecifiedWordAndLanguage(dataParts[1], lang);
					if (imageNameData1 != null) {
						imageNames.add(imageNameData1);
					}
				}
			}
			else {
				if (rebusImagePuzzleService != null) {
					String imageName = rebusImagePuzzleService.getImageBySpecifiedWordAndLanguage(queryData, lang);
					if (imageName != null) {
						imageNames.add(imageName);
					}
				}
			}
    	}

		return imageNames;
	}
	
}
