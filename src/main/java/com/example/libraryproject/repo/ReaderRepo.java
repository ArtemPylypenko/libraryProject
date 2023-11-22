package com.example.libraryproject.repo;

import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.entity.UserAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReaderRepo extends CrudRepository<Reader, Long> {
    @Query(value = "select * from readers where email = ?1", nativeQuery = true)
    Optional<Reader> findByEmail(String email);
    @Query(value = "delete from readers where email = ?1", nativeQuery = true)
    void deleteReaderByEmail(String email);
}
