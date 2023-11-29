package com.example.libraryproject.services;

import com.example.libraryproject.entity.Book;
import com.example.libraryproject.entity.BookHistory;
import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.repo.BookHistoryRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookHistoryService {
    private final BookHistoryRepo bookHistoryRepo;
    private final BookService bookService;

    public boolean existsByBookAndUser(Long book, Long user) {
        return bookHistoryRepo.existsBookUser(book, user).isPresent();
    }

    public void deleteAllByBook(Book book) {

    }

    @Transactional
    public void addReaderBook(Reader reader, Book book) {

        reader.getBooks().add(book);
        book.getReaders().add(reader);

//        bookHistoryRepo.save(bookHistory);
    }

    @Transactional
    public void deleteUserBooksByBook(Book book) {
        bookHistoryRepo.deleteByBook(book);
    }

    @Transactional
    public void deleteUserBooksByUser(Reader reader) {
        bookHistoryRepo.deleteByReader(reader);
    }

}
