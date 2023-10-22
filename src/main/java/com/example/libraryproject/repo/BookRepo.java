package com.example.libraryproject.repo;

import com.example.libraryproject.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepo extends CrudRepository<Book, Long> {
   
}
