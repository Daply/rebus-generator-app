package com.rebusgenerator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rebusgenerator.entity.Rebus;
import com.rebusgenerator.repository.RebusRepository;

/**
 * 
 * @author Daria Pleshchankova
 *
 */
@Service
public class RebusService {

	@Autowired
	RebusRepository rebusRepository;
	
	public Rebus getRebus(String word) {
		return rebusRepository.findRebus(word);
	}
	
	public void save(Rebus rebus) {
		rebusRepository.save(rebus);
	}
	
}
