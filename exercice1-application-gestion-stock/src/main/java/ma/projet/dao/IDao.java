package ma.projet.dao;

import java.util.List;

public interface IDao<T> {
    T save(T t);
    T update(T t);
    void delete(T t);
    T findById(Long id);
    List<T> findAll();
}
