package com.example.libraryproject.services;

import com.example.libraryproject.entity.Book;
import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.entity.UserAuth;
import com.example.libraryproject.repo.ReaderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ReaderService implements ClassicalDao<Reader> {
    private final ReaderRepo readerRepo;
    private final UserService userService;

    @Override
    public Reader save(Reader reader) {
        userService.save(reader);
        return readerRepo.save(reader);
    }

    @Override
    public void delete(Reader reader) {
        String email = reader.getEmail();
        readerRepo.deleteReaderByEmail(email);
        userService.deleteByEmail(email);
    }

    @Override
    public List<Reader> getAll() {
        return StreamSupport.stream(readerRepo.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Optional<Reader> findByEmail(String email) {
        return readerRepo.findByEmail(email);
    }

    public void addBook(String email, Book book) {
        Optional<Reader> reader = readerRepo.findByEmail(email);
        reader.ifPresent(value -> value.addBook(book));
    }
}
