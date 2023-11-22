package com.example.libraryproject.services;

import com.example.libraryproject.entity.Book;
import com.example.libraryproject.repo.BookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BookService implements ClassicalDao<Book> {
    private final BookRepo bookRepo;

    @Override
    public Book save(Book book) {
        return bookRepo.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepo.delete(book);
    }

    @Override
    public List<Book> getAll(Book book) {
        return StreamSupport.stream(bookRepo.findAll().spliterator(), false).collect(Collectors.toList());
    }
}
