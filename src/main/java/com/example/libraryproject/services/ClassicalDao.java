package com.example.libraryproject.services;

import java.util.List;

public interface ClassicalDao<T> {
    public T save(T t);

    public void delete(T t);

    public List<T> getAll(T t);
}
