package com.rebusgenerator.algorithm;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.*;

import org.springframework.stereotype.Component;

import com.rebusgenerator.exception.ImageProcessorException;

/**
 * Process all the images groups, 
 * load needed images to temporary directory.
 * 
 * Image group contains of two horizontal layers,
 * as on the first layer must be database word
 * that encrypts needed word part and images
 * that match front and back queries, on the second
 * layer there are images that match middle queries.
 * 
 * @author Daria Pleshchankova
 *
 */
@Component
public class ImageProcessor {
	
	/**
	 * Logger
	 */
	private final static org.apache.log4j.Logger LOGGER = Logger.getLogger(ImageProcessor.class);
	
	/**
	 * Orientation in what two images
	 * will be merged
	 */
	enum Orientation {
		VERTICAL, HORIZONTAL
	}
	
	/**
	 * Image extensions
	 *  png or jpeg
	 *
	 */
	enum ImageExtension {
		PNG, JPEG;
		
		public String getString() { 
			if (this == PNG) {
				return "PNG";
			}
			if (this == JPEG) {
				return "JPEG";
			}
			return "PNG";
		}
		
		public String getExtension() { 
			if (this == PNG) {
				return ".png";
			}
			if (this == JPEG) {
				return ".jpeg";
			}
			return ".png";
		}
	}
	
	
	/**
	 * Combine all images to one final image
	 * 
	 * @param word - word, from which needed to create a rebus
	 * @param lang - word language
	 * @param images - list of images names
	 * @param imagesDirectory - temporary folder to where images will be stored
	 * @return final image path
	 * @throws IOException
	 * @throws ImageProcessorException 
	 */
	public String mergeAllWordsImagesGroups(String word, String lang, List<List<List<String>>> images, String imagesDirectory) throws IOException, ImageProcessorException {
		
        LOGGER.info("Final image processing...");
		
		if (word == null || lang == null || images == null ||
				imagesDirectory == null) {
			LOGGER.error("Some of arguments are null!");
			if (lang == null && lang == null && images == null &&
					imagesDirectory == null)
				throw new ImageProcessorException("all arguments are null");
			else if (word == null)
				throw new ImageProcessorException("word is null");
			else if (lang == null) 
				throw new ImageProcessorException("lang is null");
			else if (images == null) 
				throw new ImageProcessorException("images is null");
			else if (imagesDirectory == null) 
				throw new ImageProcessorException("images directory is null");
			 
			throw new ImageProcessorException();
		}
	
		if (images.isEmpty()) {
			LOGGER.error("The images list is empty!");
			throw new ImageProcessorException("images list is empty");
		}
		
		List<String> groupsTotalImages = new ArrayList<String>();
		int imageGroupNumber = 0;
		for (List<List<String>> wordImageGroup: images) {
			if (wordImageGroup == null)
				throw new ImageProcessorException("images list is empty");
			groupsTotalImages.add(mergeOneWordsImagesGroup(wordImageGroup, imageGroupNumber, imagesDirectory));
			imageGroupNumber++;
		}
		String finalImageName = word + "_" + lang;
		String nameOfFinalImage = mergeListOfImagesInOneDirection(groupsTotalImages, Orientation.HORIZONTAL, imagesDirectory, finalImageName);
		
		// delete all images from directory, except the final image
		deleteAllImages(nameOfFinalImage, imagesDirectory);		
		
		LOGGER.info("Final image processed successfully");
		
		return imagesDirectory + "/" + nameOfFinalImage;
	}
	
	/**
	 * Merge one image group
	 * 
	 * @param wordImageGroup - list of two layers with list of images in each
	 * @param imageGroupNumber - number of image group
	 * @param imagesDirectory - directory where images are stored
	 * @return result image name
	 * @throws IOException
	 * @throws ImageProcessorException 
	 */
	public String mergeOneWordsImagesGroup(List<List<String>> wordImageGroup, int imageGroupNumber, String imagesDirectory) throws IOException, ImageProcessorException {
		
		if (wordImageGroup == null || imagesDirectory == null) {
			LOGGER.error("Some of arguments are null!");
			if (wordImageGroup == null && imagesDirectory == null)
				throw new ImageProcessorException("all arguments are null");
			else if (wordImageGroup == null)
				throw new ImageProcessorException("word image group is null");
			else if (imagesDirectory == null)
				throw new ImageProcessorException("images directory is null");
		}
		
		if (wordImageGroup.isEmpty()) {
			LOGGER.error("The word image group list is empty!");
			throw new ImageProcessorException("word image group list is empty");
		}
		
		String imageResult = null;
		String combinedNameResult = null;
		// process first layer
		combinedNameResult = "group_" + imageGroupNumber + "_layer_1";
		String imageUp = mergeListOfImagesInOneDirection(wordImageGroup.get(0), Orientation.HORIZONTAL, imagesDirectory, combinedNameResult);
		imageResult = imageUp;
		// process second layer
		if (wordImageGroup.size() > 1) {
			combinedNameResult = "group_" + imageGroupNumber + "_layer_2";
			String imageDown = mergeListOfImagesInOneDirection(wordImageGroup.get(1), Orientation.HORIZONTAL, imagesDirectory, combinedNameResult);
			combinedNameResult = "group_" + imageGroupNumber + "_result";
			imageResult = mergeTwoImages(imageUp, imageDown, Orientation.VERTICAL, 20, imagesDirectory, combinedNameResult);
		}
		return imageResult;
	}
	
	/**
	 * Merge a group of images in in specified orientation
	 * 
	 * @param namesOfImages - list of images names 
	 * @param orientation - orientation in what images will be merged
	 * @param imagesDirectory - directory where images are stored
	 * @param finalName -  result image name
	 * @return result image name
	 * @throws IOException
	 * @throws ImageProcessorException 
	 */
	public String mergeListOfImagesInOneDirection(List<String> namesOfImages, 
			Orientation orientation, String imagesDirectory, String finalName) throws IOException, ImageProcessorException {
		
		if (namesOfImages == null || imagesDirectory == null ||
				finalName == null) {
			LOGGER.error("Some of arguments are null!");
			if (namesOfImages == null && imagesDirectory == null &&
					finalName == null)
				throw new ImageProcessorException("all arguments are null");
			else if (namesOfImages == null)
				throw new ImageProcessorException("names of images list is null");
			else if (imagesDirectory == null)
				throw new ImageProcessorException("images directory is null");
			else if (finalName == null)
				throw new ImageProcessorException("final image name is null");
		}

		if (namesOfImages.isEmpty()) {
			LOGGER.error("The names of images list is empty!");
			throw new ImageProcessorException("names of images list is empty");
		}
		
		String imageOneName = null;
		String combinedNameResult = null;
		int imagesCounter = 0;
		for (String imageName: namesOfImages) {
			if (imageName == null)
				throw new ImageProcessorException("image name is null");
			if (imageOneName == null) {
				imageOneName = imageName;
			}
			else {
				if (imagesCounter == namesOfImages.size() - 1) {
					combinedNameResult = finalName;
				}
				else {
					combinedNameResult = imageOneName.substring(0, imageOneName.length() - 4) + "_" +
							imageName.substring(0, imageName.length() - 4);
				}
				imageOneName = mergeTwoImages(imageOneName, imageName, orientation, 20, imagesDirectory, combinedNameResult);
			}
			imagesCounter++;
		}
		// if there were only one image, rename it to final image name
		if (combinedNameResult == null) {
			String name = renameImage(imageOneName, finalName, imagesDirectory);
			imageOneName = name;
		}
		return imageOneName;
	}
	
	/**
	 * Merge two images in specified orientation
	 * 
	 * @param imageOneName - first image
	 * @param imageTwoName - scond image
	 * @param orientation - orientation in what two images will be merged
	 * @param distanceBetweenImages - set the distance between images
	 * @param imagesDirectory - directory where images are stored
	 * @param combinedNameResult - result image name
	 * @return result image name
	 * @throws IOException
	 * @throws ImageProcessorException 
	 */
	public String mergeTwoImages(String imageOneName, String imageTwoName, Orientation orientation,
			int distanceBetweenImages, String imagesDirectory, String combinedNameResult) throws IOException, ImageProcessorException {
		
		if (imageOneName == null || imageTwoName == null ||
				imagesDirectory == null || combinedNameResult == null) {
			LOGGER.error("Some of arguments are null!");
			if (imageOneName == null && imageTwoName == null &&
					imageTwoName == null && combinedNameResult == null)
				throw new ImageProcessorException("all arguments are null");
			else if (imageOneName == null)
				throw new ImageProcessorException("image one name is null");
			else if (imagesDirectory == null)
				throw new ImageProcessorException("images directory is null");
			else if (combinedNameResult == null)
				throw new ImageProcessorException("combined name result is null");
		}
		
		if (!checkIfDirectoryExists(imagesDirectory)) {
			LOGGER.error("Images directory does not exist!");
			throw new ImageProcessorException("directory does not exist");
		}
		
		BufferedImage imageOne = ImageIO.read(new File(imagesDirectory, imageOneName));
		BufferedImage imageTwo = ImageIO.read(new File(imagesDirectory, imageTwoName));
		int resultWidth = 0;
		int resltHeight = 0;
		int xFirst = 0;
		int yFirst = 0;
		int xSecond = 0;
		int ySecond = 0;
		switch (orientation) {
		   case VERTICAL:
			   resultWidth = Math.max(imageOne.getWidth(), imageTwo.getWidth());
			   resltHeight = imageOne.getHeight() + distanceBetweenImages + imageTwo.getHeight();
			   ySecond = imageOne.getHeight() + distanceBetweenImages;
			   if (imageOne.getWidth() > imageTwo.getWidth()) {
				   xSecond = imageOne.getWidth()/2 - imageTwo.getWidth()/2;
			   }
			   else {
				   xFirst = imageTwo.getWidth()/2 - imageOne.getWidth()/2;
			   }	   
			   break;
		   case HORIZONTAL:
			   resultWidth = imageOne.getWidth() + distanceBetweenImages + imageTwo.getWidth();
			   resltHeight = Math.max(imageOne.getHeight(), imageTwo.getHeight());
			   xSecond = imageOne.getWidth() + distanceBetweenImages;
			   if (imageOne.getHeight() > imageTwo.getHeight()) {
				   ySecond = imageOne.getHeight()/2 - imageTwo.getHeight()/2;
			   }
			   else {
				   yFirst = imageTwo.getHeight()/2 - imageOne.getHeight()/2;
			   }		   
			   break;
		   default:
			   resultWidth = Math.max(imageOne.getWidth(), imageTwo.getWidth());
			   resltHeight = Math.max(imageOne.getHeight(), imageTwo.getHeight());
		}
		BufferedImage combined = new BufferedImage(resultWidth, resltHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics g = combined.getGraphics();
		g.drawImage(imageOne, xFirst, yFirst, null);
		g.drawImage(imageTwo, xSecond, ySecond, null);
		String newFileName = combinedNameResult + ImageExtension.PNG.getExtension();
		ImageIO.write(combined, ImageExtension.PNG.getString(), 
				new File(imagesDirectory, newFileName));
		return newFileName;
	}
	
	public boolean checkIfDirectoryExists(String directory) {
		File check = new File(directory + "/test.txt");
		try {
			if (!check.createNewFile()) {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		check.delete();
		return true;
	}
	
	public void deleteAllImages(String finalImageName, String directory) {
		File dir = new File(directory);
		for (File file: dir.listFiles()) { 
		    if (!file.getName().equals(finalImageName)) { 
		        file.delete();
		    }
		}
	}
	
	public String renameImage(String currentImageName, String newImageName, String directory) throws IOException {
		File file = new File(directory + "/" + currentImageName);
		byte[] fileContent = Files.readAllBytes(file.toPath());
		String newImageNameWithExt = newImageName + ImageExtension.PNG.getExtension();
		File newFile = new File(directory + "/" + newImageNameWithExt);
        OutputStream os = new FileOutputStream(newFile); 
        os.write(fileContent); 
        os.close();
		return newImageNameWithExt;
	}
	    
}
