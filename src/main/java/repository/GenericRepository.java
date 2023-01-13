package repository;

import java.io.Serializable;
import java.util.List;

public interface GenericRepository<T, ID> {
    T save(T entity);
    void saveAll(List<T> entities);
    T update(T entity);
    T findById(ID id);
    boolean existsById(ID id);
    List<T> findAll();
    Long count();
    void deleteById(ID id);
    void delete(T entity);
    void deleteAll();
}
