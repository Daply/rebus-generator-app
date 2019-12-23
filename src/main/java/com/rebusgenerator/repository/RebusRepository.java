package com.rebusgenerator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rebusgenerator.entity.Rebus;

/**
 * 
 * @author Pleshchankova Daria
 *
 */
public interface RebusRepository extends CrudRepository<Rebus, Long> {
	
	@Query(value = "SELECT r FROM Rebus r")
    List<Rebus> findAll();
    
    Optional<Rebus> findById(Long id);
    
    @Query(value = "SELECT * FROM rebus r WHERE r.rebus_word=:word", nativeQuery = true)
    Rebus findRebus(@Param("word") String word);
}
