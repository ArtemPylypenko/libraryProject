package com.example.libraryproject.services;

import com.example.libraryproject.entity.Book;
import com.example.libraryproject.entity.BookHistory;
import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.repo.BookHistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookHistoryService {
    BookHistoryRepo bookHistoryRepo;
    BookService bookService;

    boolean existsByBookAndUser(Book book, Reader user) {
        return bookHistoryRepo.existsBookUser(book.getId(), user.getId()).isPresent();
    }

    void deleteAllByBook(Book book){

    }

}
