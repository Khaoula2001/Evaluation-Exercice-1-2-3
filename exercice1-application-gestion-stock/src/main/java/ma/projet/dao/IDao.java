package ma.projet.dao;

import java.util.List;

public interface IDao<T, ID> {
    boolean create(T o);
    boolean update(T o);
    boolean delete(T o);
    T getById(ID id);
    List<T> getAll();
}
