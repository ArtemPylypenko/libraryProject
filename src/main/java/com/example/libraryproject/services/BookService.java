package com.example.libraryproject.services;

import com.example.libraryproject.entity.Book;
import com.example.libraryproject.repo.BookReaderRepo;
import com.example.libraryproject.repo.BookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BookService implements ClassicalDao<Book> {
    private final BookRepo bookRepo;
    private final BookReaderRepo bookHistoryRepo;

    @Override
    public Book save(Book book) {
        return bookRepo.save(book);
    }

    @Override
    public void delete(Book book) {
        bookHistoryRepo.deleteByBook(book);
        bookRepo.deleteById(book.getId());
    }

    @Override
    public List<Book> getAll() {
        return StreamSupport.stream(bookRepo.findAll().spliterator(), false).toList();
    }

    public Optional<Book> getById(Long id) {
        return bookRepo.findById(id);
    }

    public void deleteById(Long id) {
        delete(getById(id).get());
    }

    public void updateById(String name, String authors, int publication, String isbn, String given_by, Long id) {
        bookRepo.updateBook(name, authors, publication, isbn, given_by, id);
    }

    public void updateAvailable(boolean available, Long id) {
        bookRepo.updateAvailable(available, id);
    }

    public void updateRating(Double rating, Long id) {
        bookRepo.updateRating(rating, id);
    }
}
