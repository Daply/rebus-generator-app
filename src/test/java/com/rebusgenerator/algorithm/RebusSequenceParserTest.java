package com.rebusgenerator.algorithm;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.rebusgenerator.exception.ParserException;

public class RebusSequenceParserTest {
	
	@Test
	public void testParseRebusSequenceWithNullArguments() {
		RebusSequenceParser rebusSequenceParser = new RebusSequenceParser();
		ParserException e = assertThrows(ParserException.class, () -> {
			rebusSequenceParser.parseRebusSequence(null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in parsing a rebus sequence: all arguments are null"));
	}
	
	@Test
	public void testProcessWordGroupByLayersWithNullArguments() {
		RebusSequenceParser rebusSequenceParser = new RebusSequenceParser();
		ParserException e = assertThrows(ParserException.class, () -> {
			rebusSequenceParser.processWordGroupByLayers(null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in parsing a rebus sequence: all arguments are null"));
	}
	
	@Test
	public void testProcessQueryLayerWithNullArguments() {
		RebusSequenceParser rebusSequenceParser = new RebusSequenceParser();
		ParserException e = assertThrows(ParserException.class, () -> {
			rebusSequenceParser.processQueryLayer(null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in parsing a rebus sequence: all arguments are null"));
	}
	
	@Test
	public void testProcessOneQueryWithNullArguments() {
		RebusSequenceParser rebusSequenceParser = new RebusSequenceParser();
		ParserException e = assertThrows(ParserException.class, () -> {
			rebusSequenceParser.processOneQuery(null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in parsing a rebus sequence: all arguments are null"));
	}
	
	@Test
	public void testProcessOneQueryWithDifferentArguments() {
		RebusSequenceParser rebusSequenceParser = new RebusSequenceParser();
		List<List<String>> expected = new ArrayList<List<String>>();
		expected.add(new ArrayList<String>());
		expected.add(new ArrayList<String>());
		expected.add(new ArrayList<String>());
		expected.add(new ArrayList<String>());
		List<List<String>> actual = new ArrayList<List<String>>();
		try {
			actual.add(rebusSequenceParser.processOneQuery("", ""));
			actual.add(rebusSequenceParser.processOneQuery("qgaegeS []csd[v[", "fewfe"));
			actual.add(rebusSequenceParser.processOneQuery("[[[[[[[[]]]]]]]", "en"));
			actual.add(rebusSequenceParser.processOneQuery("[front[]lnknkn, ", "5=r"));
		} catch (ParserException e) {
			e.printStackTrace();
		}
		assertThat(actual, is(expected));
	}
	
	@Test
	public void testProcessQueryCommandWithNullArguments() {
		RebusSequenceParser rebusSequenceParser = new RebusSequenceParser();
		ParserException e = assertThrows(ParserException.class, () -> {
			rebusSequenceParser.processQueryCommand(null, null, null, null);
		});
		assertTrue(e.getMessage().contains("Exceptional situation in parsing a rebus sequence: all arguments are null"));
	}
	
	@Test
	public void testProcessQueryCommandWithDifferentArguments() {
		RebusSequenceParser rebusSequenceParser = new RebusSequenceParser();
		List<List<String>> expected = new ArrayList<List<String>>();
		expected.add(new ArrayList<String>());
		expected.add(new ArrayList<String>());
		expected.add(new ArrayList<String>());
		expected.add(new ArrayList<String>());
		List<List<String>> actual = new ArrayList<List<String>>();
		try {
			actual.add(rebusSequenceParser.processQueryCommand("", "", "", ""));
			actual.add(rebusSequenceParser.processQueryCommand("wfrs[[", "rgsrg", "rgr", "rgrdg"));
			actual.add(rebusSequenceParser.processQueryCommand("delete", "5=r", "back", "fwflfml"));
			actual.add(rebusSequenceParser.processQueryCommand("front", "5=r", "delete", "en"));
		} catch (ParserException e) {
			e.printStackTrace();
		}
		assertThat(actual, is(expected));
	}
}
