package repository;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

public interface GenericRepository<T, ID extends Serializable> {
    T save(T entity);
    T findById(ID id);
    boolean existsById(ID id);
    List<T> findAll();
    Long count();
    void deleteById(ID id);
    void delete(T entity);
    void deleteAll();
}
