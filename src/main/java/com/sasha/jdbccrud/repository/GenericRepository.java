package com.sasha.jdbccrud.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {
    Optional<T> save(T entity);
    void saveAll(List<T> entities);
    Optional<T> update(T entity);
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    Optional<List<T>> findAll();
    void deleteById(ID id);
    void delete(T entity);
    void deleteAll();
}
