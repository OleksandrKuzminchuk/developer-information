package com.sasha.jdbccrud.service;

import java.util.List;

public interface GenericService<T, ID> {
    T save(T entity);
    void saveAll(List<T> entities);
    T update(T entity);
    T findById(ID id);
    boolean existsById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    void delete(T entity);
    void deleteAll();
}
