package com.example.libraryproject.services;

import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.repo.BookHistoryRepo;
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
    private final BookHistoryRepo bookHistoryRepo;

    @Override
    public Reader save(Reader reader) {
        userService.save(reader);
        return readerRepo.save(reader);
    }

    @Override
    public void delete(Reader reader) {
        String email = reader.getEmail();
        bookHistoryRepo.deleteByReader(reader);
        readerRepo.deleteReaderByEmail(email);
        userService.deleteByEmail(email);
    }

    public void deleteById(Long id) {
        delete(getById(id).get());
    }

    @Override
    public List<Reader> getAll() {
        return StreamSupport.stream(readerRepo.findAll().spliterator(), false).collect(Collectors.toList());
    }

    public Optional<Reader> findByEmail(String email) {
        return readerRepo.findByEmail(email);
    }

    public Optional<Reader> getById(Long id) {
        return readerRepo.findById(id);
    }

    public void update(String email, String password, String name, String surname, String phone, String placeToLive, Long id) {
        readerRepo.update(email, password, name, surname, phone, placeToLive, id);
    }
}
