package com.rebusgenerator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rebusgenerator.entity.Language;

/**
 * 
 * @author Pleshchankova Daria
 *
 */
public interface LanguageRepository extends CrudRepository<Language, Long> {
	
	@Query(value = "SELECT l FROM Language l")
    List<Language> findAll();
    
    Optional<Language> findById(Long id);
    
    @Query(value = "SELECT * FROM Language WHERE lang=:lang", nativeQuery = true)
    Language findLanguageByLangAbbr(@Param("lang") String lang);
    
    @Query(value = "SELECT lang FROM Language", nativeQuery = true)
    List<String> findAllLanguages();
}
