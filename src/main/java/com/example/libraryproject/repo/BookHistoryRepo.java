package com.example.libraryproject.repo;

import com.example.libraryproject.entity.Book;
import com.example.libraryproject.entity.BookHistory;
import com.example.libraryproject.entity.Reader;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookHistoryRepo extends CrudRepository<BookHistory, Long> {


    @Query(value = "select * from book_history where book_id = ?1 and reader_id = ?2", nativeQuery = true)
    Optional<BookHistory> existsBookUser(Long book, Long reader);

    @Modifying
    @Transactional
    @Query("DELETE FROM BookHistory bh WHERE bh.book = :book")
    void deleteByBook(Book book);

    @Modifying
    @Transactional
    @Query("DELETE FROM BookHistory bh WHERE bh.reader = :reader")
    void deleteByReader(Reader reader);

    @Modifying
    @Transactional
    @Query("UPDATE BookHistory bh SET bh.updatedAt = CURRENT_TIMESTAMP  WHERE bh.book = :book and bh.reader = :reader")
    void updateReturnTime(Book book, Reader reader);

    @Modifying
    @Transactional
    @Query("UPDATE BookHistory bh SET bh.createdAt = CURRENT_TIMESTAMP  WHERE bh.book = :book and bh.reader = :reader")
    void updateGiveTime(Book book, Reader reader);

}
