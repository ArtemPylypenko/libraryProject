package com.example.libraryproject.repo;

import com.example.libraryproject.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BookRepo extends CrudRepository<Book, Long> {
    @Override
    Iterable<Book> findAll();

    @Query(value = "select available from books where name = ?1 and authors = ?2", nativeQuery = true)
    boolean isAvailable(String bookName, String authors);

}
