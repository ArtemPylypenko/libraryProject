package com.example.libraryproject.services;

import com.example.libraryproject.entity.History;
import com.example.libraryproject.repo.BookRepo;
import com.example.libraryproject.repo.HistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        historyRepo.updateRating(rating, readerId, bookID);
        historyRepo.updateReturn(readerId, bookID);
    }

    public Double getAVGRating(Long bookId) {
        Double avgRating = historyRepo.getAVGRating(bookId);
        if (avgRating == null)
            return null;
        else
            return (double) Math.round(avgRating * 10) / 10;
    }

    public void updateRating() {
        StreamSupport.stream(bookRepo.findAll().spliterator(), false).toList().forEach(book -> {
            if (getAVGRating(book.getId()) != null)
                bookRepo.updateRating(getAVGRating(book.getId()), book.getId());
        });
    }

    public List<History> getReaderHistory(Long reader) {
        return historyRepo.getAllByReader(reader);
    }

    public List<History> getHistoryAfter(LocalDateTime date, Long reader) {
        return historyRepo.getAllByReader(reader).stream().filter(h -> h.getCreatedAt().isAfter(date)).toList();
    }

    public List<History> getHistoryBefore(LocalDateTime date, Long reader) {
        return historyRepo.getAllByReader(reader).stream().filter(h -> h.getCreatedAt().isBefore(date)).toList();
    }

    public List<History> getHistoryBetween(LocalDateTime start, LocalDateTime end, Long reader) {
        return historyRepo.getAllByReader(reader).stream().filter(h ->
                h.getCreatedAt().isAfter(start) && h.getCreatedAt().isBefore(end)).toList();
    }
}
