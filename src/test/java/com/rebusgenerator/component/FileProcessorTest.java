package com.rebusgenerator.component;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rebusgenerator.aws.AwsStorageService;
import com.rebusgenerator.component.FileProcessor;
import com.rebusgenerator.exception.FileLoadingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileProcessorTest {
	
	@Value("${test.file.name}")
	private String TEST_FILE;
	
	@Value("${aws.bucket}")
	private String bucketName;
	
	@Autowired
	FileProcessor fileProcessingService;
	
	@Autowired
	AwsStorageService awsStorageService;
	
	@Test
	public void testSaveUploadedFileForReviewWithNullArguments() {
		FileLoadingException e = assertThrows(FileLoadingException.class, () -> {
			fileProcessingService.saveUploadedFileForReview(null, null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in file loading: all arguments are null"));
	}
	
	@Test
	public void testUploadFileWithNullArguments() throws IOException, FileLoadingException {
		FileLoadingException e = assertThrows(FileLoadingException.class, () -> {
			fileProcessingService.uploadFile(null, null, null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in file loading: all arguments are null"));
	}
	
	@Test
	public void testSaveUploadedFileWithNullArguments() {
		FileLoadingException e = assertThrows(FileLoadingException.class, () -> {
			fileProcessingService.saveUploadedFile(null, null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in file loading: all arguments are null"));
	}
	
	@Test
	public void testDownloadFilesWithNullArguments() {
		FileLoadingException e = assertThrows(FileLoadingException.class, () -> {
			fileProcessingService.downloadFiles(null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in file loading: all arguments are null"));
	}
	
	@Test
	public void testDownloadFileWithNullArguments() {
		FileLoadingException e = assertThrows(FileLoadingException.class, () -> {
			fileProcessingService.downloadFile(null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in file loading: all arguments are null"));
	}
	
	@Test
	public void testDownloadFileWithTestFileUploaded() throws IOException, FileLoadingException {
		uploadTestFile();
		assertTrue(fileProcessingService.downloadFile(TEST_FILE, TEST_FILE.substring(0, TEST_FILE.indexOf("."))));
		deleteTestFile();
	}
	
	private void uploadTestFile() throws IOException {
		File newFile = File.createTempFile(TEST_FILE.substring(0, TEST_FILE.indexOf(".")), 
				TEST_FILE.substring(TEST_FILE.indexOf("."), TEST_FILE.length()));
		awsStorageService.uploadFile(bucketName, TEST_FILE, newFile);
		newFile.delete();
	}
	
	private void deleteTestFile() throws IOException {
		awsStorageService.deleteFile(bucketName, TEST_FILE);
	}
	
}
