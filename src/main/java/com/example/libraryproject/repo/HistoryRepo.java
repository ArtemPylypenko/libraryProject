package com.example.libraryproject.repo;

import com.example.libraryproject.entity.History;
import org.springframework.data.repository.CrudRepository;

public interface HistoryRepo extends CrudRepository<History, Long> {
}
