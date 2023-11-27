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

    String giveBookUser(Book book, Reader user) {
        if (existsByBookAndUser(book, user)) {
            return "User already have book";
        } else {
            BookHistory history = new BookHistory();
            history.setBook(book);
            history.setUser(user);
            history.setTakeTime(LocalDateTime.now());
            bookHistoryRepo.save(history);
            return "Book has been given!";
        }
    }

//    String returnBook(Book book, User user){
//
//    }

}
