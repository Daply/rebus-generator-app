package com.rebusgenerator.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rebusgenerator.entity.RebusUser;

public interface UserRepository extends CrudRepository<RebusUser, Long> {
    
    @Query(value = "SELECT * FROM rebus_user u WHERE u.username=:username", 
            nativeQuery = true)
    RebusUser findByUsername(@Param("username") String username);     

}
