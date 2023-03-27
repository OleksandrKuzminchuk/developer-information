package com.sasha.hibernate.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {
    Optional<T> save(T entity);
    Optional<T> update(T entity);
    Optional<T> findById(ID id);
    Optional<List<T>> findAll();
    void deleteById(ID id);
    void deleteAll();
}
