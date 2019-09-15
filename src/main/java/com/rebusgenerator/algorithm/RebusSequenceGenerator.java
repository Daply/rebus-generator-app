package com.rebusgenerator.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.*;

import org.springframework.stereotype.Component;

import com.rebusgenerator.exception.AlgorithmException;

/**
 * Class to process the algorithm of
 * creating a rebus for given word
 * 
 * @author Daria Pleshchankova
 *
 */
@Component
public class RebusSequenceGenerator {
	
	/**
	 * Logger
	 */
	private final static org.apache.log4j.Logger LOGGER = Logger.getLogger(RebusSequenceGenerator.class);

	public RebusSequenceGenerator() {
	}

	/**
	 * The algorithm:
	 * 
	 * 1. Divide the word, from what is needed to create 
	 * a rebus, into parts from 2 to word length sequential
	 * symbols.
	 * 
	 * 2. Compare each part with existing words in a database 
	 * and save as a map of index of where the part starts and
	 * RebusDictionary object, that represents a word part, the
	 * similar to it word from database and number of steps needed
	 * to change the word from database to get this word part.
	 * 
	 * 3. Go through all created map and dictionaries (RebusDictionary)
	 * and make sequences of nodes, where node is a RebusDictionary
	 * object, that can be connected to the next node, which word part 
	 * starts, where the current node ends. So it will be created kind
	 * of chain of parts of the word, from what it is needed to create
	 * a rebus.
	 * 
	 * 4. Choose the best chain of nodes (with the smallest overall
	 * number of steps, needed to change the word from database to
	 * get the word part).
	 * 
	 * 5. Create from a chain of nodes a list of queries for modfications
	 * of database words.
	 *
	 * 
	 * @param word - the word needed to encrypt to rebus
	 * @return
	 * @throws AlgorithmException 
	 */
	public List<String> generateRebus(String word, Collection<String> databaseWords) throws AlgorithmException {
		
		LOGGER.info("Generating rebus...");
		
		if (word == null || databaseWords == null) {
			LOGGER.error("Some of arguments are null!");
			if (word == null && databaseWords == null)
				throw new AlgorithmException("all arguments are null");
			else if (word == null)
				throw new AlgorithmException("word is null");
			else if (databaseWords == null)
				throw new AlgorithmException("database words are null");
		}
		
		if (word.matches("(.+\\s.+)+")) {
			LOGGER.warn("The word has spaces");
			throw new AlgorithmException("word has spaces");
		}
		
		if (databaseWords.isEmpty()) {
			LOGGER.error("The databaseWords is empty, it is impossible to create a rebus!");
			throw new AlgorithmException("database words list is empty");
		}

		// dividing word on several parts
		List<List<WordPart>> allWordParts = divideWordOnParts(word);
	    
		// map of index where part starts and list of dictionary of this part
		Map<Integer, List<RebusDictionary>> mapIndexListOfDicts = new HashMap<>();
		List<RebusDictionary> dictionaryOfSimilarWords = new ArrayList<RebusDictionary>();
		RebusDictionary dictionary = null;
		for (List<WordPart> parts: allWordParts) {
			for (String databaseWord: databaseWords) {
				if (databaseWord == null)
					throw new AlgorithmException("database word is null in database words list");
				for (WordPart part: parts) {
					int numberOfStepsToChangeWord = findNumberOfStepsToChangeWord(databaseWord, part.getWordPart());
					int index = part.getStartIndex();
					dictionary = new RebusDictionary(part.getWordPart(), index, databaseWord, 
							numberOfStepsToChangeWord);
		
					// add to map
					if (mapIndexListOfDicts.containsKey(index)) {
						List<RebusDictionary> dicts = mapIndexListOfDicts.get(index);
						dicts.add(dictionary);
						mapIndexListOfDicts.put(index, dicts);
					}
					else {
						List<RebusDictionary> dicts = new ArrayList<RebusDictionary>();
						dicts.add(dictionary);
						mapIndexListOfDicts.put(index, dicts);
					}
					
					dictionaryOfSimilarWords.add(dictionary);
				}	
			}
		}

		// adding chain of word parts as nodes
		RebusDictionary root = createSequenceOfWordParts(word, dictionaryOfSimilarWords, mapIndexListOfDicts);
	
		// recover all the sequence of rebus building
		List<String> rebus = restoreChainOfWordParts(root);
		
		LOGGER.info("Rebus generated successfully");
		
		return rebus;
	}
	
	/**
	 * Divide the word to encrypt to rebus on parts (from 2 letters 
	 * to all word length letters) for comparing parts of word with 
	 * existing words in database.
	 * 
	 * Example:  hello
	 *           he el ll lo
	 *           hel ell llo
	 *           hell ello
	 *  
	 * @param word - word, from which needed to create a rebus
	 * @return list of part of word, from which needed to create a rebus
	 * @throws AlgorithmException 
	 */
	public List<List<WordPart>> divideWordOnParts(String word) throws AlgorithmException {
		
		if (word == null) {
			LOGGER.error("The word in divideWordOnParts method is null!");
			throw new AlgorithmException("word is null");
		}
		
		int wordLength = word.length();
		int minimumPartLength = 2;
		int maximumPartLength = wordLength - minimumPartLength;

		List<List<WordPart>> allWordParts = new ArrayList<List<WordPart>>();
		List<WordPart> wordPartsWithOneSize = new ArrayList<WordPart>();
		WordPart wordPart = new WordPart(word, 0);
		wordPartsWithOneSize.add(wordPart);
		allWordParts.add(wordPartsWithOneSize);
		for (int i = minimumPartLength; i <= maximumPartLength; i++) {
			wordPartsWithOneSize = new ArrayList<WordPart>();
			for (int j = 0; j + i <= word.length(); j++) {
				wordPart = new WordPart(word.substring(j, j + i), j);
				wordPartsWithOneSize.add(wordPart);
			}
			allWordParts.add(wordPartsWithOneSize);
		}
		
		return allWordParts;
	}
	
	/**
	 * Levenshtein Distance algorithm.
	 * 
	 * Finding the minimum number of steps to modify the word from database
	 * to get a word, from which needed to create a rebus.
	 * 
	 * @param databaseWord - word from database
	 * @param word - word, from which needed to create a rebus
	 * @return minimum number of steps needed to modify the word from
	 *         the database to get a word, from which needed to create a rebus
	 * @throws AlgorithmException 
	 */
	public int findNumberOfStepsToChangeWord(String databaseWord, String word) throws AlgorithmException {
		
		if (word == null || databaseWord == null) {
			LOGGER.error("Some of arguments are null!");
			if (databaseWord == null && word == null)
				throw new AlgorithmException("all arguments are null");
			else if (word == null)
				throw new AlgorithmException("word is null");
			else if (databaseWord == null)
				throw new AlgorithmException("database word is null");
		}
		
	    int[][] dp = new int[databaseWord.length() + 1][word.length() + 1];
	 
	    for (int i = 0; i <= databaseWord.length(); i++) {
	        for (int j = 0; j <= word.length(); j++) {
	            if (i == 0) {
	                dp[i][j] = j;
	            }
	            else if (j == 0) {
	                dp[i][j] = i;
	            }
	            else {
	                dp[i][j] = min(dp[i - 1][j - 1] 
	                 + costOfSubstitution(databaseWord.charAt(i - 1), word.charAt(j - 1)), 
	                  dp[i - 1][j] + 1, 
	                  dp[i][j - 1] + 1);
	            }
	        }
	    }
	 
	    return dp[databaseWord.length()][word.length()];
	}
	
	
	private int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
	
	private int min(int... numbers) {
        return Arrays.stream(numbers)
          .min().orElse(Integer.MAX_VALUE);
    }
	
	/**
	 * 1. Creating a sequences of nodes.
	 * Node is a part of word with the saved the most similar word from the database,
	 * index from which this part starts in a given word and connected node.
	 * Connected node is a part of word that goes right from the current pert of node.
	 * The whole chain of nodes must fit to whole word.
	 * 
	 * 2. Finding within all sequences the one, which has the minimal summing number
	 * of changes needed to modify all parts of chain to get a goal words from datatbase.
	 *
	 * Example:
	 *   phe + nome + na (The root node is 'phe', 'phe' contains connected node 'nome' and
	 *   'nome' contains connected node 'na')
	 * 
	 * @param word - word, from which needed to create a rebus
	 * @param listOfDictionaries - list of all parts found
	 * @param mapIndexListOfDicts - map of parts with their start indexes in a given word as a key
	 * @return node which starts the chain with all parts of word, 
	 *         from which needed to create a rebus, wih the minimal
	 *         summing number of steps needed to modify each word
	 *         part to the goal word from the database
	 * @throws AlgorithmException 
	 */
	public RebusDictionary createSequenceOfWordParts(String word,
			List<RebusDictionary> listOfDictionaries,
			Map<Integer, List<RebusDictionary>> mapIndexListOfDicts) throws AlgorithmException {	
		
		if (word == null || listOfDictionaries == null || mapIndexListOfDicts == null) {
			LOGGER.error("Some of arguments are null!");
			if (word == null && listOfDictionaries == null && mapIndexListOfDicts == null)
				throw new AlgorithmException("all arguments are null");
			else if (word == null) 
				throw new AlgorithmException("word is null");
			else if (listOfDictionaries == null)
				throw new AlgorithmException("listOfDictionaries is null");
			else if (mapIndexListOfDicts == null)
				throw new AlgorithmException("mapIndexListOfDicts is null");
		}
		
		// list of all connected nodes
		List<RebusDictionary> nodes = new ArrayList<>();
		
		List<RebusDictionary> theMostFitRightParts = new ArrayList<RebusDictionary>();
		for (RebusDictionary dict: listOfDictionaries) {
			if (dict == null)
				throw new AlgorithmException("dictionary is null in list of dictionaries list");
			theMostFitRightParts = new ArrayList<RebusDictionary>();
			int start = dict.getStartIndex();
			int end = start + dict.goalWord.length();
			if (mapIndexListOfDicts.containsKey(end)) {
				for (RebusDictionary rightDict: mapIndexListOfDicts.get(end)) {
					if (!rightDict.goalWord.contentEquals(dict.goalWord)) {
						theMostFitRightParts.add(rightDict);
					}
				}
			}
			// sorting by number steps to the goal word
			Collections.sort(theMostFitRightParts);
			
			// connecting nodes
			if (!theMostFitRightParts.isEmpty()) {
				dict.rightRebusDictionaryNode = theMostFitRightParts.get(0);
			}
			
			if (dict.rightRebusDictionaryNode != null || 
					(dict.rightRebusDictionaryNode == null &&
					dict.numberOfStepsToTheGoal <= 2)) {
				nodes.add(dict);
			}
		}
		
		// for each node get a connected chain of nodes
		// and summ all number of steps needed to modify each part
		// to get a goal word
		//
		// search from all the nodes the one with minimal cost, 
		// where cost is a summing number of steps needed to mofify each part
		// and a coverage length of given word
		int minimumCost = word.length()*11;
		int currentNumberOfStepToGoal = 0;
		int coverageLength = 0;
		int totalCost = 0; // minimum number of steps to goal + coverage length
		RebusDictionary minimalNode = null;
		for (RebusDictionary dictionary: nodes) {
			RebusDictionary currentNode = dictionary;
			currentNumberOfStepToGoal = 0;
			coverageLength = 0;
			while (currentNode != null) {
				currentNumberOfStepToGoal += currentNode.numberOfStepsToTheGoal;
				coverageLength += currentNode.goalWordLength;
				currentNode = currentNode.rightRebusDictionaryNode;
			}
			totalCost = currentNumberOfStepToGoal + (word.length() - coverageLength);
			if (word.length() == coverageLength && totalCost < minimumCost) {
				minimumCost = totalCost;
				minimalNode = dictionary;
			}
		}
		
		return minimalNode;
	}

	/**
	 * Getting root node, which is the most suitable chain
	 * of word parts to create a rebus and restoring the whole rebus
	 * sequence.
	 * 
	 * @param node - root node of the chain
	 * @return a list of rebus sequence
	 * @throws AlgorithmException 
	 */
	public List<String> restoreChainOfWordParts(RebusDictionary node) throws AlgorithmException {
		
		LOGGER.info("Creation of queries chain started...");
		
		if (node == null) {
			LOGGER.error("The node in restoreChainOfWordParts method is null!");
			throw new AlgorithmException("node is null");
		}
		
		List<String> rebusSequence = new ArrayList<>();
		RebusDictionary root = node;
        while (root != null) {
        	rebusSequence.addAll(createSequenceOfChangesToGetOneWrdFromAnother(root.theMostSimilarWord, root.goalWord));
        	root = root.rightRebusDictionaryNode;
        	if (root != null) {
        		rebusSequence.add("+");
        	}
        }
        return rebusSequence;
	}
	
	/**
	 * Create a part of rebus sequence with one word part,
	 * from which needed to create a rebus.
	 * 
	 * queries:
	 * [front delete]      - delete letter in front of the word
	 * [front change] a=b  - delete letter in front of the word
	 * [front insert] a    - insert letter delete letter in front of the word
	 * 
	 * [middle delete] a   - delete letter a
	 * [middle change] a=b - change letter a in word to b
	 * [middle insert] 2=a - insert to position 2 in word letter a
	 * 
	 * [back delete]       - delete letter from back of the word
	 * [back insert] a     - insert a letter in back of the word
	 * 
	 * @param databaseWord - word to modify
	 * @param word - goal word
	 * @return
	 * @throws AlgorithmException 
	 */
	public List<String> createSequenceOfChangesToGetOneWrdFromAnother(String databaseWord, String word) throws AlgorithmException {
		
		if (databaseWord == null || word == null) {
			LOGGER.error("Some of arguments are null!");
			if (databaseWord == null && word == null) 
				throw new AlgorithmException("all arguments are null");
			else if (databaseWord == null)
				throw new AlgorithmException("database word is null");
			else if (word == null)
				throw new AlgorithmException("word is null");
		}
		
		// 1. Get intersection of chars of two words
		// 2. Resolve all characters before starting the coincidence of two words
		// 3. Resolve all characters during the coincidence of two words
		// 4. Resolve all characters after the coincidence of two words
		
		//// 1.		
		// 1.1. get all chars and its indexes in database word and 
		// in given rebus word:
		// gather lists of coincided sequences of chars
		List<Character> commonCharsSequence = getCommomCharSequenceOfTwoWords(databaseWord, word);;
		
		//// 2.
		Stack<String> rebusSequence = new Stack<>();
		
		// sometimes there can be a situation, when there is no common chars in word,
		// so it needs to be checked whether the common chars sequence is not empty
		int indexOfDatabaseWord = 0;
		int indexOfWord = 0;
		if (commonCharsSequence != null && !commonCharsSequence.isEmpty()) {
			int indexOfCommonChars = 0;
			int indexOfStartedCharDatabaseWord = databaseWord.indexOf(commonCharsSequence.get(indexOfCommonChars));
			int indexOfStartedCharWord = word.indexOf(commonCharsSequence.get(indexOfCommonChars));
			
			if (indexOfStartedCharDatabaseWord > indexOfStartedCharWord) {
				int difference = indexOfStartedCharDatabaseWord - indexOfStartedCharWord;
				for (int i = 0; i < difference; i++) {
					rebusSequence.push("[front delete]");
				}
				for (int i = difference; i < indexOfStartedCharDatabaseWord; i++) {
					rebusSequence.push("[front change] " + databaseWord.charAt(i) + "=" + word.charAt(i-difference));
				}
			}
			else {
				for (int i = 0; i < indexOfStartedCharDatabaseWord; i++) {
					rebusSequence.push("[front delete]");
				}
				for (int i = 0; i < indexOfStartedCharWord; i++) {
					rebusSequence.push("[front insert] " + word.charAt(i));
				}
			}
			// put the database word to the stack
			rebusSequence.push(databaseWord);
			
			//// 3.
			int indexOfList = 0;
			indexOfDatabaseWord = indexOfStartedCharDatabaseWord;
			indexOfWord = indexOfStartedCharWord;
			
			if (indexOfDatabaseWord > -1 && indexOfWord > -1) {
				while (indexOfDatabaseWord < databaseWord.length() &&
						indexOfWord < word.length()) {
					if (databaseWord.charAt(indexOfDatabaseWord) == word.charAt(indexOfWord) &&
							databaseWord.charAt(indexOfDatabaseWord) == commonCharsSequence.get(indexOfList)) {
						commonCharsSequence.remove(indexOfList);
						indexOfDatabaseWord++;
						indexOfWord++;
					}
					else {
						if (commonCharsSequence.isEmpty()) {
							break;
						}
						
						// if database word matches common character but given word is not 
						// make an insert character in database word
						if (databaseWord.charAt(indexOfDatabaseWord) == commonCharsSequence.get(indexOfList)) {
							rebusSequence.push("[middle insert] " + indexOfDatabaseWord + "=" + word.charAt(indexOfWord));
							indexOfWord++;
						}
						// if given word matches common character but database word is not 
						// make an delete character from database word
						else if (word.charAt(indexOfWord) == commonCharsSequence.get(indexOfList)) {
							rebusSequence.push("[middle delete] " + databaseWord.charAt(indexOfDatabaseWord));
							indexOfDatabaseWord++;
						}
						else if (databaseWord.charAt(indexOfDatabaseWord) != word.charAt(indexOfWord) &&
								databaseWord.charAt(indexOfDatabaseWord) != commonCharsSequence.get(indexOfList) &&
								word.charAt(indexOfWord) != commonCharsSequence.get(indexOfList)) {
							rebusSequence.push("[middle change] " + databaseWord.charAt(indexOfDatabaseWord) + "=" + word.charAt(indexOfWord));
							indexOfDatabaseWord++;
							indexOfWord++;
						}
					}
				}
			}
		}
		else {
			// put the database word to the stack
			rebusSequence.push(databaseWord);
		}
		
		//// 4.
		if (indexOfDatabaseWord < databaseWord.length()) {
			for (int i = indexOfDatabaseWord; i < databaseWord.length(); i++) {
				rebusSequence.push("[back delete]");
			}
		}
		if (indexOfWord < word.length()) {
			for (int i = indexOfWord; i < word.length(); i++) {
				rebusSequence.push("[back insert] " + word.charAt(i));
			}
		}
		
		List<String> stackAsList = new ArrayList<>(rebusSequence);
        return stackAsList;
	}
	
	/**
	 * Find a sequence of common chars in
	 * two words
	 * 
	 * @param oneWord
	 * @param secondWord
	 * @return
	 * @throws AlgorithmException
	 */
	public List<Character> getCommomCharSequenceOfTwoWords(String oneWord, String secondWord) throws AlgorithmException {
		
		if (oneWord == null || secondWord == null) {
			LOGGER.error("Some of arguments are null!");
			if (oneWord == null && secondWord == null) 
				throw new AlgorithmException("all arguments are null");
			else
				throw new AlgorithmException("some of arguments are null");
		}
		
		String searchWord = null;
		String baseWord = null;
		if (oneWord.length() < secondWord.length()) {
			searchWord = oneWord;
			baseWord = secondWord;
		}
		else {
			searchWord = secondWord;
			baseWord = oneWord;
		}
		
		List<Sequence> allSequences = new ArrayList<>();
		List<Sequence> tempSequences = new ArrayList<>();
		for (int i = 0; i < baseWord.length(); i++) {
			for (int j = 0; j < searchWord.length(); j++) {
				char baseWordChar = baseWord.charAt(i);
				char searchWordChar = searchWord.charAt(j);
				if (baseWordChar == searchWordChar) {
					Sequence seq = new Sequence(i, j);
					seq.addCharacter(searchWordChar);
					tempSequences = new ArrayList<>();
					for (Sequence prevSeq: allSequences) {
						if (prevSeq.indexI < i && prevSeq.indexJ < j) {
							Sequence newSeq = new Sequence(i, j);
							newSeq.addCharacters(prevSeq.getSequenceOfCharacters());
							newSeq.addCharacter(searchWordChar);
							tempSequences.add(newSeq);
						}
					}
					allSequences.add(seq);
					allSequences.addAll(tempSequences);
				}
			}
		}
		
		Collections.sort(allSequences);
		
		List<Character> commonCharsSequence = new ArrayList<Character>();
		if (!allSequences.isEmpty()) {
			commonCharsSequence = allSequences.get(0).getSequenceOfCharacters();
		}
		
		return commonCharsSequence;	
	}
	
	/**
	 * Help class for findind common char 
	 * sequences
	 * 
	 * @author Daria Pleshchankova
	 *
	 */
	public class Sequence implements Comparable<Sequence>{
		private List<Character> sequenceOfCharacters = null;
		private int indexI = 0;
		private int indexJ = 0;
		
		public Sequence(int i, int j) {
			sequenceOfCharacters = new ArrayList<>();
			indexI = i;
			indexJ = j;
		}

		public void addCharacter(char ch) {
			sequenceOfCharacters.add(ch);
		}
		
		public void addCharacters(List<Character> list) {
			sequenceOfCharacters.addAll(list);
		}
		
		public List<Character> getSequenceOfCharacters() {
			return sequenceOfCharacters;
		}

		public void setSequenceOfCharacters(List<Character> sequenceOfCharacters) {
			this.sequenceOfCharacters = sequenceOfCharacters;
		}

		public int getIndexI() {
			return indexI;
		}

		public void setIndexI(int indexI) {
			this.indexI = indexI;
		}

		public int getIndexJ() {
			return indexJ;
		}

		public void setIndexJ(int indexJ) {
			this.indexJ = indexJ;
		}

		@Override
		public int compareTo(Sequence o) {
			if (this.sequenceOfCharacters.size() < o.sequenceOfCharacters.size())
				return 1;
			else if (this.sequenceOfCharacters.size() > o.sequenceOfCharacters.size())
				return -1;
			return 0;
		}

		@Override
		public String toString() {
			return "Sequence [sequenceOfCharacters=" + sequenceOfCharacters + ", indexI=" + indexI + ", indexJ="
					+ indexJ + "]";
		}
		
		
	}
	
	
	/**
	 * Class that represents part of entire word with the index,
	 * where this part starts
	 * 
	 * @author Daria Pleshchankova
	 *
	 */
	public class WordPart {
		private String wordPart;
		private int startIndex;
		private int endIndex;
		
		public WordPart(String wordPart, int index) {
			this.wordPart = wordPart;
			this.startIndex = index;
		}
		
		public String getWordPart() {
			return wordPart;
		}
		public void setWordPart(String wordPart) {
			this.wordPart = wordPart;
		}
		public int getStartIndex() {
			return startIndex;
		}
		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}
		public int getEndIndex() {
			return endIndex;
		}
		public void setEndIndex(int endIndex) {
			this.endIndex = endIndex;
		}
		
	}
	
	
	/**
	 * The class that represents a word or a part of word (goalWord),
	 * with the start index (startIndex) of some bigger word, its
	 * length (goalWordLength), the word from database found the 
	 * most similar to the goal word and number of steps needed to modify
	 * the most similar word to get a goal word
	 * 
	 * @author Daria Pleshchankova
	 *
	 */
	public class RebusDictionary implements Comparable<RebusDictionary>{
		private String goalWord;
		private int startIndex;
		private int goalWordLength;
		private String theMostSimilarWord;
		private int numberOfStepsToTheGoal;
		
		// the part of given word that lays on the right side of current part
		public RebusDictionary rightRebusDictionaryNode;
		
		public RebusDictionary() {
			
		}
		
		public RebusDictionary(String goalWord, int startIndex, String theMostSimilarWord, int numberOfStepsToTheGoal) {
			this.goalWord = goalWord;
			this.startIndex = startIndex;
			this.goalWordLength = goalWord.length();
			this.theMostSimilarWord = theMostSimilarWord;
			this.numberOfStepsToTheGoal = numberOfStepsToTheGoal;
		}
		
		public String getGoalWord() {
			return goalWord;
		}
		public String getTheMostSimilarWord() {
			return theMostSimilarWord;
		}
		public int getStartIndex() {
			return startIndex;
		}

		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}

		public int getNumberOfStepsToTheGoal() {
			return numberOfStepsToTheGoal;
		}
		public void setGoalWord(String goalWord) {
			this.goalWord = goalWord;
		}
		public void setTheMostSimilarWord(String theMostSimilarWord) {
			this.theMostSimilarWord = theMostSimilarWord;
		}
		public void setNumberOfStepsToTheGoal(int numberOfStepsToTheGoal) {
			this.numberOfStepsToTheGoal = numberOfStepsToTheGoal;
		}

		public int getGoalWordLength() {
			return goalWordLength;
		}

		public void setGoalWordLength(int goalWordLength) {
			this.goalWordLength = goalWordLength;
		}

		public RebusDictionary getRightRebusDictionaryNode() {
			return rightRebusDictionaryNode;
		}

		public void setRightRebusDictionaryNode(RebusDictionary rightRebusDictionaryNode) {
			this.rightRebusDictionaryNode = rightRebusDictionaryNode;
		}

		@Override
		public String toString() {
			return "RebusDictionary [goalWord=" + goalWord + ", theMostSimilarWord=" + theMostSimilarWord
					+ ", numberOfStepsToTheGoal=" + numberOfStepsToTheGoal + "]";
		}

		@Override
		public int compareTo(RebusDictionary obj) {
			if (this.numberOfStepsToTheGoal > obj.numberOfStepsToTheGoal)
				return 1;
			else if (this.numberOfStepsToTheGoal == obj.numberOfStepsToTheGoal)
				return 0;
			return -1;
		}
		
	}
}
