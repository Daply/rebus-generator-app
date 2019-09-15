package com.rebusgenerator.algorithm;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.rebusgenerator.algorithm.ImageProcessor.Orientation;
import com.rebusgenerator.exception.ImageProcessorException;

public class ImageProcessorTest {
	
	@Test
	public void testMergeAllWordsImagesGroupsWithNullArguments() {
		ImageProcessor imageProcessor = new ImageProcessor();
		ImageProcessorException e = assertThrows(ImageProcessorException.class, () -> {
			imageProcessor.mergeAllWordsImagesGroups(null, null, null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in processing the final rebus image: all arguments are null"));
	}
	
	@Test
	public void testMergeAllWordsImagesGroupsWithOneNullArgument() {
		ImageProcessor imageProcessor = new ImageProcessor();
		ImageProcessorException e = assertThrows(ImageProcessorException.class, () -> {
			imageProcessor.mergeAllWordsImagesGroups("phenomena", "en", null, "/images");
		});
		assertTrue(e.getMessage().contains("Exceptional situation in processing the final rebus image: images is null"));
	}
	
	@Test
	public void testMergeOneWordsImagesGroupWithNullArguments() {
		ImageProcessor imageProcessor = new ImageProcessor();
		ImageProcessorException e = assertThrows(ImageProcessorException.class, () -> {
			imageProcessor.mergeOneWordsImagesGroup(null, 0, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in processing the final rebus image: all arguments are null"));
	}
	
	@Test
	public void testMergeOneWordsImagesGroupWithOneNullArgument() {
		ImageProcessor imageProcessor = new ImageProcessor();
		ImageProcessorException e = assertThrows(ImageProcessorException.class, () -> {
			imageProcessor.mergeOneWordsImagesGroup(null, 0, "/images");
		});
		assertTrue(e.getMessage().contains("Exceptional situation in processing the final rebus image: word image group is null"));
	}
	
	@Test
	public void testMergeListOfImagesInOneDirectionWithNullArguments() {
		ImageProcessor imageProcessor = new ImageProcessor();
		ImageProcessorException e = assertThrows(ImageProcessorException.class, () -> {
			imageProcessor.mergeListOfImagesInOneDirection(null, Orientation.HORIZONTAL, null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in processing the final rebus image: all arguments are null"));
	}
	
	@Test
	public void testMergeListOfImagesInOneDirectionWithOneNullArgument() {
		ImageProcessor imageProcessor = new ImageProcessor();
		ImageProcessorException e = assertThrows(ImageProcessorException.class, () -> {
			imageProcessor.mergeListOfImagesInOneDirection(new ArrayList<String>(), Orientation.HORIZONTAL, "/images", null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in processing the final rebus image: final image name is null"));
	}
	
	@Test
	public void testMergeTwoImagesWithNullArguments() {
		ImageProcessor imageProcessor = new ImageProcessor();
		ImageProcessorException e = assertThrows(ImageProcessorException.class, () -> {
			imageProcessor.mergeTwoImages(null, null, Orientation.HORIZONTAL, 0, null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in processing the final rebus image: all arguments are null"));
	}
	
	@Test
	public void testMergeTwoImagesWithOneNullArgument() {
		ImageProcessor imageProcessor = new ImageProcessor();
		ImageProcessorException e = assertThrows(ImageProcessorException.class, () -> {
			imageProcessor.mergeTwoImages("image1.png", "image2.png", Orientation.HORIZONTAL, 0, null, "final");
		});
		assertTrue(e.getMessage().contains("Exceptional situation in processing the final rebus image: images directory is null"));
	}
	
	@Test
	public void testMergeTwoImagesWithNotExistingDirectory() {
		ImageProcessor imageProcessor = new ImageProcessor();
		ImageProcessorException e = assertThrows(ImageProcessorException.class, () -> {
			imageProcessor.mergeTwoImages("image1.png", "image2.png", Orientation.HORIZONTAL, 0, "/images", "final");
		});
		assertTrue(e.getMessage().contains("Exceptional situation in processing the final rebus image: directory does not exist"));
	}
}
