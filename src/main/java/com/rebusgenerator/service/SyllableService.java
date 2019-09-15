package com.rebusgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rebusgenerator.entity.Syllable;
import com.rebusgenerator.repository.SyllableRepository;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Service
public class SyllableService {
	
	@Autowired
	SyllableRepository syllableRepository;
	
	public Syllable findSyllable(String syllable) {
		return syllableRepository.findSyllable(syllable);
	}
	
	public void save(Syllable syllable) {
		syllableRepository.save(syllable);
	}

}
