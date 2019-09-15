package com.rebusgenerator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rebusgenerator.entity.Syllable;

/**
 * 
 * @author Pleshchankova Daria
 *
 */
public interface SyllableRepository extends CrudRepository<Syllable, Long> {
	
	@Query(value = "SELECT l FROM Syllable l")
    List<Syllable> findAll();
    
    Optional<Syllable> findById(Long id);
    
    @Query(value = "SELECT * FROM Syllable s WHERE s.syllable=:syllable", nativeQuery = true)
    Syllable findSyllable(@Param("syllable") String syllable);
}
