package com.rebusgenerator.algorithm;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.rebusgenerator.exception.AlgorithmException;

public class RebusSequenceGeneratorTest {

	@Test
	public void testGenerateRebusWithNullArguments() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.generateRebus(null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: all arguments are null"));
	}
	
	@Test
	public void testGenerateRebusWithOneNullArgument() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.generateRebus("phenome na", null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: database words are null"));
	}
	
	@Test
	public void testGenerateRebusWithWordArgumentWithSpaces() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		List<String> dbWords = new ArrayList<String>();
		dbWords.add("phone");
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.generateRebus("phenome na", dbWords);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: word has spaces"));
	}
	
	@Test
	public void testGenerateRebusWithEmptyDatabaseWordsArgument() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.generateRebus("phenomena", new ArrayList<String>());
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: database words list is empty"));
	}
	
	@Test
	public void testDivideWordOnPartsWithNullArgument() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.divideWordOnParts(null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: word is null"));
	}
	
	@Test
	public void testFindNumberOfStepsToChangeWordWithNullArguments() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.findNumberOfStepsToChangeWord(null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: all arguments are null"));
	}
	
	@Test
	public void testFindNumberOfStepsToChangeWordWithOneNullArgument() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.findNumberOfStepsToChangeWord(null, "phenomena");
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: database word is null"));
	}
	
	@Test
	public void testFindNumberOfStepsToChangeWordWithDifferentArguments() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		List<Integer> expectedValues = Arrays.asList(1, 5, 4, 3, 6);
		List<Integer> actualValues = new ArrayList<Integer>();
		try {
			actualValues.add(rebusGeneratorAlgorithm.findNumberOfStepsToChangeWord("hello", "herllo"));
			actualValues.add(rebusGeneratorAlgorithm.findNumberOfStepsToChangeWord("satisfaction", "satrimectio"));
			actualValues.add(rebusGeneratorAlgorithm.findNumberOfStepsToChangeWord("attribute", "tribe"));
			actualValues.add(rebusGeneratorAlgorithm.findNumberOfStepsToChangeWord("creature", "theatre"));
			actualValues.add(rebusGeneratorAlgorithm.findNumberOfStepsToChangeWord("sun", "unicorn"));
		} catch (AlgorithmException e) {
			e.printStackTrace();
		}
		assertThat(actualValues, is(expectedValues));
	}
	
	@Test
	public void testCreateSequenceOfWordPartsWithAllNullArguments() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.createSequenceOfWordParts(null, null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: all arguments are null"));
	}
	
	@Test
	public void testCreateSequenceOfWordPartsWithOneNullArgument() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.createSequenceOfWordParts("phenomena", null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: listOfDictionaries is null"));
	}
	
	@Test
	public void testRestoreChainOfWordPartsWithAllNullArguments() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.restoreChainOfWordParts(null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: node is null"));
	}
	
	@Test
	public void testCreateSequenceOfChangesToGetOneWrdFromAnotherWithAllNullArguments() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.createSequenceOfChangesToGetOneWrdFromAnother(null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: all arguments are null"));
	}
	
	@Test
	public void testCreateSequenceOfChangesToGetOneWrdFromAnotherWithOneNullArgument() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.createSequenceOfChangesToGetOneWrdFromAnother(null, "phenomena");
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: database word is null"));
	}
	
	@Test
	public void testCreateSequenceOfChangesToGetOneWrdFromAnotherWithDifferentArguments() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		List<List<String>> expected = new ArrayList<List<String>>();
		expected.add(Arrays.asList("hello", "[middle insert] 3=r"));
		expected.add(Arrays.asList("satisfaction", "[middle insert] 4=r",
				"[middle change] s=m", "[middle change] f=e", "[middle delete] a",
				"[back delete]"));
		expected.add(Arrays.asList("[front delete]", "esssseerrttt", 
				"[middle delete] s", "[middle delete] s", "[middle delete] s",
				"[middle delete] e", "[middle delete] e", "[middle delete] r", 
				"[back delete]", "[back delete]"));
		expected.add(Arrays.asList("[front delete]", "attribute", 
				"[middle delete] t", "[middle delete] u", "[middle delete] t"));
		expected.add(Arrays.asList("[front delete]", "[front delete]", 
				"[front insert] t", "[front insert] h", "creature", 
				"[middle delete] u"));
		expected.add(Arrays.asList("[front delete]", "sun", 
				"[back insert] i", "[back insert] c", "[back insert] o", 
				"[back insert] r", "[back insert] n"));
		List<List<String>> actual = new ArrayList<List<String>>();
		try {
			actual.add(rebusGeneratorAlgorithm.createSequenceOfChangesToGetOneWrdFromAnother("hello", "herllo"));
		    actual.add(rebusGeneratorAlgorithm.createSequenceOfChangesToGetOneWrdFromAnother("satisfaction", "satrimectio"));
		    actual.add(rebusGeneratorAlgorithm.createSequenceOfChangesToGetOneWrdFromAnother("esssseerrttt", "srt"));
			actual.add(rebusGeneratorAlgorithm.createSequenceOfChangesToGetOneWrdFromAnother("attribute", "tribe"));
			actual.add(rebusGeneratorAlgorithm.createSequenceOfChangesToGetOneWrdFromAnother("creature", "theatre"));
			actual.add(rebusGeneratorAlgorithm.createSequenceOfChangesToGetOneWrdFromAnother("sun", "unicorn"));
		} catch (AlgorithmException e) {
			e.printStackTrace();
		}
		assertThat(actual, is(expected));
	}
	
	@Test
	public void testGetCommomCharSequenceOfTwoWordsWithNullArguments() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		AlgorithmException e = assertThrows(AlgorithmException.class, () -> {
			rebusGeneratorAlgorithm.getCommomCharSequenceOfTwoWords(null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in algorithm of creating a rebus: all arguments are null"));
	}
	
	@Test
	public void testGetCommomCharSequenceOfTwoWordsWithDifferentArguments() {
		RebusSequenceGenerator rebusGeneratorAlgorithm = new RebusSequenceGenerator();
		List<List<Character>> expected = new ArrayList<List<Character>>();
		expected.add(Arrays.asList('h', 'e', 'l', 'l', 'o'));
		expected.add(Arrays.asList('s', 'a', 't', 'i', 'c', 't', 'i', 'o'));
		expected.add(Arrays.asList('i', 'f', 'c', 't', 'i', 'o', 'n'));
		expected.add(Arrays.asList('s', 'r', 't'));
		expected.add(Arrays.asList('t', 'r', 'i', 'b', 'e'));
		expected.add(Arrays.asList('t', 'h', 'e', 'a', 't', 'r', 'e'));
		expected.add(Arrays.asList('u', 'n'));
		List<List<Character>> actual = new ArrayList<>();
		try {
			actual.add(rebusGeneratorAlgorithm.getCommomCharSequenceOfTwoWords("hello", "herllo"));
		    actual.add(rebusGeneratorAlgorithm.getCommomCharSequenceOfTwoWords("satisfaction", "satrimectio"));
		    actual.add(rebusGeneratorAlgorithm.getCommomCharSequenceOfTwoWords("esatisfaction", "infection"));
		    actual.add(rebusGeneratorAlgorithm.getCommomCharSequenceOfTwoWords("esssseerrttt", "srt"));
			actual.add(rebusGeneratorAlgorithm.getCommomCharSequenceOfTwoWords("attribute", "tribe"));
			actual.add(rebusGeneratorAlgorithm.getCommomCharSequenceOfTwoWords("tchhreature", "theatre"));
			actual.add(rebusGeneratorAlgorithm.getCommomCharSequenceOfTwoWords("sun", "unicorn"));
		} catch (AlgorithmException e) {
			e.printStackTrace();
		}
		assertThat(actual, is(expected));
	}
}
