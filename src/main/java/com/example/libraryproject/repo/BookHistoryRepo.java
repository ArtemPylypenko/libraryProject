package com.example.libraryproject.repo;

import com.example.libraryproject.entity.BookHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookHistoryRepo extends CrudRepository<BookHistory, Long> {


    @Query(value = "select * from book_history where book_id = ?1 and user_id = ?2", nativeQuery = true)
    Optional<BookHistory> existsBookUser(Long book, Long user);

}
