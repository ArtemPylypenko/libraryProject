package com.example.libraryproject.services;

import com.example.libraryproject.entity.Librarian;
import com.example.libraryproject.repo.LibrarianRepo;

import java.util.List;
import java.util.stream.StreamSupport;

public class LibrarianService implements ClassicalDao<Librarian> {
    LibrarianRepo librarianRepo;
    UserService userService;

    @Override
    public Librarian save(Librarian librarian) {
        userService.save(librarian);
        return librarianRepo.save(librarian);
    }

    @Override
    public void delete(Librarian librarian) {
        String email = librarian.getEmail();
        librarianRepo.deleteReaderByEmail(email);
        userService.deleteByEmail(email);
    }

    @Override
    public List<Librarian> getAll(Librarian librarian) {
        return StreamSupport.stream(librarianRepo.findAll().spliterator(), false).toList();

    }
}
