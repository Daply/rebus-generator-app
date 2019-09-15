package com.rebusgenerator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rebusgenerator.entity.RebusImagePuzzle;

/**
 * 
 * @author Pleshchankova Daria
 *
 */
public interface RebusImagePuzzleRepository extends CrudRepository<RebusImagePuzzle, Long> {
	
	@Query(value = "SELECT r FROM RebusImagePuzzle r")
    List<RebusImagePuzzle> findAll();
	
	Optional<RebusImagePuzzle> findById(Long id); 
	
	@Query(value = "SELECT r.image_word FROM rebus_image_puzzle r INNER JOIN language l ON r.word_lang=l.language_id WHERE " + 
			"l.lang=:lang AND r.image_word_type=0", 
            nativeQuery = true)
	List<String> findAllWordsByLang(@Param("lang") String lang);

	@Query(value = "SELECT rw.image_word FROM (SELECT * FROM rebus_image_puzzle r USE INDEX (wordind) WHERE r.image_word LIKE %:word%) rw " + 
			"INNER JOIN language l ON rw.word_lang=l.language_id WHERE rw.image_word_type=0 AND l.lang=:lang", 
            nativeQuery = true)
	List<String> findAllTheMostSimilarWords(@Param("word") String word, @Param("lang") String lang);
	
	@Query(value = "SELECT r.image_word FROM rebus_image_puzzle r INNER JOIN language l ON r.word_lang=l.language_id WHERE " + 
			"l.lang=:lang AND r.image_word_type=1", 
            nativeQuery = true)
	List<String> findAllLettersByLang(@Param("lang") String lang);
	
	@Query(value = "SELECT r.image_name FROM rebus_image_puzzle r INNER JOIN language l ON r.word_lang=l.language_id WHERE " + 
			"l.lang=:lang AND r.image_word=:word", 
            nativeQuery = true)
	String findImageByWordAndLang(@Param("word") String word, @Param("lang") String lang);

}
