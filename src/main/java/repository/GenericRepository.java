package repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {
    T save(T entity);
    void saveAll(List<T> entities);
    T update(T entity);
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    List<T> findAll();
    Long count();
    void deleteById(ID id);
    void delete(T entity);
    void deleteAll();
}
