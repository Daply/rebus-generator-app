package com.rebusgenerator.aws;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Service
public class S3UploadService implements AwsStorageService{
	
	@Value("${aws.access.key.id}")
	private String accessKey;
	
	@Value("${aws.secret.key}")
	private String secretKey;
	
	private AmazonS3 s3client;
	
	@PostConstruct
	private void init() {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		this.s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_2)
				  .build();
	}
	
	@Override
	public S3ObjectInputStream downloadFile(String bucketName, String fileName) throws IOException {
		S3Object s3object = s3client.getObject(bucketName, fileName);
		return s3object.getObjectContent();
	}
	
	@Override
	public void uploadFile(String bucketName, String fileName, File file) {
		s3client.putObject(bucketName, fileName, file);
	}
	
	@Override
	public void deleteFile(String bucketName, String fileName) {
		if (s3client.doesObjectExist(bucketName, fileName))
			s3client.deleteObject(bucketName, fileName);
	}

}
