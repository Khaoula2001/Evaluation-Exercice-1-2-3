package ma.projet.service;

import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Function;

public abstract class BaseService<T> implements IDao<T> {
    private final Class<T> clazz;

    protected BaseService(Class<T> clazz) {
        this.clazz = clazz;
    }

    protected <R> R inTransaction(Function<Session, R> fn) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            R res = fn.apply(s);
            tx.commit();
            return res;
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            s.close();
        }
    }

    @Override
    public T create(T o) {
        return inTransaction(s -> { s.save(o); return o; });
    }

    @Override
    public T update(T o) { return inTransaction(s -> { s.update(o); return o; }); }

    @Override
    public void delete(Long id) {
        inTransaction(s -> { T ent = s.get(clazz, id); if (ent!=null) s.delete(ent); return null; });
    }

    @Override
    public T findById(Long id) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try { return s.get(clazz, id); } finally { s.close(); }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try { return s.createQuery("from " + clazz.getSimpleName()).list(); } finally { s.close(); }
    }
}
