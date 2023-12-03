package com.example.libraryproject.services;

import com.example.libraryproject.entity.History;
import com.example.libraryproject.repo.HistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class HistoryService implements ClassicalDao<History> {

    private final HistoryRepo historyRepo;

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
}
