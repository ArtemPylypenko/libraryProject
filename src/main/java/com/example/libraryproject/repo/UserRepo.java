package com.example.libraryproject.repo;

import com.example.libraryproject.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<UserAuth, Long> {
    @Query(value = "select * from users where email = ?1", nativeQuery = true)
    Optional<UserAuth> findByEmail(String email);


}
