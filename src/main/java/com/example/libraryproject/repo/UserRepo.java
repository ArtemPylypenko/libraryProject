package com.example.libraryproject.repo;

import com.example.libraryproject.entity.UserAuth;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<UserAuth, Long> {
    @Query(value = "select * from users where email = ?1", nativeQuery = true)
    Optional<UserAuth> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "delete from users where email = ?1", nativeQuery = true)
    public void deleteUserAuthByEmail(String email);
}
