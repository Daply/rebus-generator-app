package com.rebusgenerator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rebusgenerator.entity.ImageWordType;
import com.rebusgenerator.entity.Language;
import com.rebusgenerator.entity.RebusImagePuzzle;
import com.rebusgenerator.entity.Syllable;
import com.rebusgenerator.repository.LanguageRepository;
import com.rebusgenerator.repository.RebusImagePuzzleRepository;
import com.rebusgenerator.repository.SyllableRepository;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Service
public class RebusImagePuzzleService {
	
	@Autowired
	RebusImagePuzzleRepository rebusImagePuzzleRepository;
	
	@Autowired
	LanguageRepository languageRepository;
	
	@Autowired
	SyllableRepository syllableRepository;
	
	public void saveWordAndItsAssociatedImage(String word, String lang) {
		// split word on syllables and save
		String image = word + "_" + lang + ".png";
		Language language = languageRepository.findLanguageByLangAbbr(lang);
		RebusImagePuzzle rebusImagePuzzle = new RebusImagePuzzle(word, image);
		rebusImagePuzzle.setImageWordType(ImageWordType.WORD);
		rebusImagePuzzle.setWordLang(language);
		for (int i = 0; i < word.length() - 1; i++) {
			String syl = word.substring(i, i+2);
			Syllable syllable = syllableRepository.findSyllable(syl);
			if (syllable == null)
			    syllable = new Syllable(syl);
			rebusImagePuzzle.addSyllable(syllable);
		}
		rebusImagePuzzleRepository.save(rebusImagePuzzle);
    }
	
	public List<String> getAllWordsOfSpecifiedLang(String lang) {
		return rebusImagePuzzleRepository.findAllWordsByLang(lang);
    }
	
	public List<String> getAllTheMostSimilarWords(String word, String lang) {
		return rebusImagePuzzleRepository.findAllTheMostSimilarWords(word, lang);
    }
	
	public String getImageBySpecifiedWordAndLanguage(String word, String lang) {
		return rebusImagePuzzleRepository.findImageByWordAndLang(word, lang);
    }
}
