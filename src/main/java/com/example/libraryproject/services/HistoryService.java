package com.example.libraryproject.services;

import com.example.libraryproject.entity.History;
import com.example.libraryproject.repo.BookRepo;
import com.example.libraryproject.repo.HistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class HistoryService implements ClassicalDao<History> {

    private final HistoryRepo historyRepo;
    private final BookRepo bookRepo;

    @Override
    public History save(History history) {
        return historyRepo.save(history);
    }

    @Override
    public void delete(History history) {
        historyRepo.delete(history);
    }

    @Override
    public List<History> getAll() {
        return StreamSupport.stream(historyRepo.findAll().spliterator(), false).toList();
    }

    public void updateRating(Double rating, Long readerId, Long bookID) {
        historyRepo.updateReturn(readerId, bookID);
        historyRepo.updeteRating(rating, readerId, bookID);
    }

    public Double getAVGRating(Long bookId) {
        return historyRepo.getAVGRating(bookId);
    }

    public void updateRating() {
        StreamSupport.stream(bookRepo.findAll().spliterator(), false).toList().forEach(book -> {
            if (getAVGRating(book.getId()) != null)
                book.setRating(Math.round(getAVGRating(book.getId()) * 10.0) / 10.0);
        });
    }
}
