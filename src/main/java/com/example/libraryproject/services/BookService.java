package com.example.libraryproject.services;

import com.example.libraryproject.repo.BookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepo bookRepo;
}
