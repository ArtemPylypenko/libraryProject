package com.example.libraryproject.repo;

import com.example.libraryproject.entity.Librarian;
import com.example.libraryproject.entity.UserAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LibrarianRepo extends CrudRepository<Librarian, Long> {
    @Query(value = "select * from librarians where email = ?1", nativeQuery = true)
    Optional<UserAuth> findByEmail(String email);

    @Query(value = "delete from librarians where email = ?1", nativeQuery = true)
    void deleteReaderByEmail(String email);
}
