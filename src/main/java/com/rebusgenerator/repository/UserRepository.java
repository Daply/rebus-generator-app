package com.rebusgenerator.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rebusgenerator.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
    
    @Query(value = "SELECT * FROM user u WHERE u.username=:username", 
            nativeQuery = true)
    User findByUsername(@Param("username") String username);     

}
