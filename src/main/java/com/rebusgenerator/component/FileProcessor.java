package com.rebusgenerator.component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.rebusgenerator.aws.AwsStorageService;
import com.rebusgenerator.exception.FileLoadingException;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Component
public class FileProcessor {
	
	/**
	 * Logger
	 */
	private final static org.apache.log4j.Logger LOGGER = Logger.getLogger(FileProcessor.class);
	
	@Value("${images.file.folder}")
	private String IMAGES_FOLDER;
	
	@Value("${review.file.folder}")
	private String REVIEW_FOLDER;
	
	@Value("${temp.file.folder}")
	private String TEMP_FOLDER;
	
	@Value("${aws.bucket}")
	private String bucketName;
	
	@Autowired
	AwsStorageService awsStorageService;
	
	/**
	 * Upload image to review folder for
	 * future reviewing and adding to storage
	 * 
	 * @param word - word, associated with image
	 * @param lang - word language
	 * @param file - image
	 * @return true if uploaded successfully
	 *         otherwise false
	 * @throws FileLoadingException
	 */
	public boolean saveUploadedFileForReview(String word, String lang, MultipartFile file) throws FileLoadingException {
		
		LOGGER.info("Saving uploaded file for review...");
		
		if (word == null || lang == null || file == null) {
			LOGGER.error("Some of arguments are null!");
			if (word == null && lang == null && file == null)
				throw new FileLoadingException("all arguments are null");
			else if (word == null)
				throw new FileLoadingException("word is null");
			else if (lang == null)
				throw new FileLoadingException("lang is null");
			else if (file == null)
				throw new FileLoadingException("file is null");
		}
		
		File curFile = null;
		try {
			// creating temporary new file name
			String oldFileName = file.getOriginalFilename(); 
			String newFileType = oldFileName
					.substring(file.getOriginalFilename().length()-4, 
							file.getOriginalFilename().length());
			// random key for not getting overwrites of existing files
			String newFileName = word + "_" + lang + "_" +
					generateHashKey(10) + newFileType;
			
			// save file to temporary directory for future 
			// reviewing
			curFile = saveUploadedFile(file, getReviewPath(), newFileName);
		} catch (Exception e) {
			throw new FileLoadingException(e.getMessage());
		}
		
		LOGGER.info("Uploaded file saved for review");
		
		return curFile.exists();
	}	
	
	/**
	 * Upload new image to storage
	 * 
	 * @param word - word, associated with image
	 * @param lang - word language
	 * @param file - image
	 * @return true if uploaded successfully,
	 *         otherwise false
	 * @throws IOException
	 * @throws FileLoadingException
	 */
	public boolean uploadNewImageForReview(String word, String lang, MultipartFile file) throws IOException, FileLoadingException {
		
		LOGGER.info("Uploading file for review ...");
		
		if (word == null || lang == null || file == null) {
			LOGGER.error("Some of arguments are null!");
			if (word == null && lang == null && file == null)
				throw new FileLoadingException("all arguments are null");
			else if (word == null)
				throw new FileLoadingException("word is null");
			else if (lang == null)
				throw new FileLoadingException("lang is null");
			else if (file == null)
				throw new FileLoadingException("file is null");
		}
		
		return uploadFile("review/" + word + "_" + generateHashKey(10), lang, file, bucketName);
	}
	
	/**
	 * Upload new image to storage
	 * 
	 * @param word - word, associated with image
	 * @param lang - word language
	 * @param file - image
	 * @return true if uploaded successfully,
	 *         otherwise false
	 * @throws IOException
	 * @throws FileLoadingException
	 */
	public boolean uploadNewImage(String word, String lang, MultipartFile file) throws IOException, FileLoadingException {
		
		LOGGER.info("Uploading file...");
		
		if (word == null || lang == null || file == null) {
			LOGGER.error("Some of arguments are null!");
			if (word == null && lang == null && file == null)
				throw new FileLoadingException("all arguments are null");
			else if (word == null)
				throw new FileLoadingException("word is null");
			else if (lang == null)
				throw new FileLoadingException("lang is null");
			else if (file == null)
				throw new FileLoadingException("file is null");
		}

		return uploadFile(word, lang, file, bucketName);
	}
	
	/**
	 * Upload new image to storage
	 * 
	 * @param word - word, associated with image
	 * @param lang - word language
	 * @param file - image
	 * @return true if uploaded successfully,
	 *         otherwise false
	 * @throws IOException
	 * @throws FileLoadingException
	 */
	public boolean uploadFile(String word, String lang, MultipartFile file, String bucketName) throws IOException, FileLoadingException {
		
		LOGGER.info("Uploading file...");
		
		if (word == null || lang == null || file == null || bucketName == null) {
			LOGGER.error("Some of arguments are null!");
			if (word == null && lang == null && file == null)
				throw new FileLoadingException("all arguments are null");
			else if (word == null)
				throw new FileLoadingException("word is null");
			else if (lang == null)
				throw new FileLoadingException("lang is null");
			else if (file == null)
				throw new FileLoadingException("file is null");
			else if (bucketName == null)
				throw new FileLoadingException("bucketName is null");
		}
		
		if (!file.isEmpty() && !bucketName.isEmpty()) {
			// temporary save uploaded file from client to directory of images
			File newFile = saveUploadedFile(file, getTemporaryPath(), file.getOriginalFilename());
			if (newFile != null && newFile.exists()) {
				String newFileType = file.getOriginalFilename()
						.substring(file.getOriginalFilename().length()-4, file.getOriginalFilename().length());
				String newFileName = word + "_" + lang + newFileType;
				try {
					awsStorageService.uploadFile(bucketName, newFileName, newFile);
				}
				catch (Exception e) {
					throw new FileLoadingException(e.getMessage());
				}
				newFile.delete();
				
				LOGGER.info("File uploaded successfully");
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Save uploaded file from client
	 * to specified directory
	 * 
	 * @param file - image
	 * @param path - path, where to save the file
	 * @param fileName - name of image
	 * @return new saved file
	 * @throws IOException
	 * @throws FileLoadingException
	 */
	public File saveUploadedFile(MultipartFile file, String path, String fileName) throws IOException, FileLoadingException {
		
		LOGGER.info("Saving uploaded file...");
		
		if (file == null || path == null) {
			LOGGER.error("Some of arguments are null!");
			if (file == null && path == null)
				throw new FileLoadingException("all arguments are null");
			else if (file == null)
				throw new FileLoadingException("file is null");
			else if (path == null)
				throw new FileLoadingException("path is null");
		}
		
		File newFile = null;
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            newFile = new File(path + "/" + fileName);           
            FileUtils.copyInputStreamToFile(new ByteArrayInputStream(bytes), newFile);
        }
        
        LOGGER.info("Uploaded file saved");
        
        return newFile;
    }

	/**
	 * Download files from storage
	 * 
	 * @param imageNames - list of image names
	 * @param directory - name of directory, where to save images
	 * @return path, where images saved
	 * @throws IOException
	 * @throws FileLoadingException
	 */
	public String downloadFiles(List<String> imageNames, String directory) throws IOException, FileLoadingException {	
		
		LOGGER.info("Downloading files...");
		
		if (imageNames == null || directory == null) {
			LOGGER.error("Some of arguments are null!");
			if (imageNames == null && directory == null)
				throw new FileLoadingException("all arguments are null");
			else if (imageNames == null)
				throw new FileLoadingException("image names list is null");
			else if (directory == null)
				throw new FileLoadingException("folder name is null");
		}
		
		if (imageNames.isEmpty()) {
			throw new FileLoadingException("image names list is empty, nothing to load");
		}
		
		for (String imageName: imageNames) {
			if (imageName == null)
				throw new FileLoadingException("image name is null");
			if (!downloadFile(imageName, directory)) {
				throw new FileLoadingException("file " + imageName + " wasn't loaded");
			}
		}
		
		LOGGER.info("Files downloaded successfully");
		
		return getImagesPath() + "/" + directory;
	}
	
	/**
	 * Download file from storage
	 * 
	 * @param imageName - image name
	 * @param directory - name of directory, where to save image
	 * @return true if file downloaded successfully,
	 *         otherwise false
	 * @throws IOException
	 * @throws FileLoadingException
	 */
	public boolean downloadFile(String imageName, String directory) throws IOException, FileLoadingException {
		
		LOGGER.info("Downloading file "  + imageName + " to directory " + directory);
		
		if (imageName == null || directory == null) {
			LOGGER.error("Some of arguments are null!");
			if (imageName == null && directory == null)
				throw new FileLoadingException("all arguments are null");
			else if (imageName == null)
				throw new FileLoadingException("image name is null");
			else if (directory == null)
				throw new FileLoadingException("directory is null");
		}
		
		File image = new File(getImagesPath() + directory + "/" + imageName);
		
		try {
			FileUtils.copyInputStreamToFile(awsStorageService.downloadFile(bucketName, imageName), image);
		}
		catch (Exception e) {
			throw new FileLoadingException("aws sStorage problem, probably file does not exist on the storage");
		}
		if (image.exists() && image.getTotalSpace() > 0) {
			LOGGER.info("File " + imageName + " downloaded to directory " + directory);
			return true;
		}
		return false;
	}
	
	private String getTemporaryPath() throws IOException {
		return TEMP_FOLDER;
	}
	
	private String getImagesPath() {
		return IMAGES_FOLDER;
	}
	
	private String getReviewPath() {
		return REVIEW_FOLDER;
	}
	
	/**
	 * Generate string for unique temporary
	 * file naming when loading images
	 * for review
	 * 
	 * @param length - length of the string
	 * @return
	 */
	private String generateHashKey(int length) {
		Random random = new Random();
		char[] arr = new char[length];
		int count = 0;
		while (count < length) {
			int r = random.nextInt(123);
			if ((r >= 48 && r <= 57) ||
					(r >= 65 && r <= 90) ||
					(r >= 97 && r <= 122)) {
				arr[count] = (char)r;
				count++;
			}
		}
		return new String(arr);
	}

}
