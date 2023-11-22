package com.example.libraryproject.repo;

import com.example.libraryproject.entity.BookHistory;
import org.springframework.data.repository.CrudRepository;

public interface BookHistoryRepo extends CrudRepository<BookHistory, Long> {

}
