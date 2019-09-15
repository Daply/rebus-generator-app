package com.rebusgenerator.aws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Service
public interface AwsStorageService {

	/**
	 * Download file from storage
	 * 
	 * @param bucketName
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	InputStream downloadFile(String bucketName, String fileName)
			                                    throws IOException;
	/**
	 * Upload file to storage 
	 * 
	 * @param bucketName
	 * @param fileName
	 * @param file
	 */
	void uploadFile(String bucketName, String fileName, File file);
	
	void deleteFile(String bucketName, String fileName);
}
