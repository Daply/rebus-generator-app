package com.rebusgenerator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rebusgenerator.repository.LanguageRepository;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Service
public class LanguageService {

	@Autowired
	private LanguageRepository languageRepository;
	
	public List<String> getAllPossbleLanguages() {
		return languageRepository.findAllLanguages();
    }
	
}
