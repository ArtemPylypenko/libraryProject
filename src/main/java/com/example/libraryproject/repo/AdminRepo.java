package com.example.libraryproject.repo;

import com.example.libraryproject.entity.Admin;
import com.example.libraryproject.entity.UserAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminRepo extends CrudRepository<Admin, Long> {
    @Query(value = "select * from admins where email = ?1", nativeQuery = true)
    Optional<UserAuth> findByEmail(String email);

}
