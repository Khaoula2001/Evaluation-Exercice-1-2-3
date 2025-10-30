package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.util.HibernateUtil;

import java.util.List;

public class CategorieService implements IDao<Categorie, Long> {
    private Session getSession(){
        return HibernateUtil.getSessionFactory().openSession();
    }
    @Override
    public boolean create(Categorie o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try {
            s.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally { s.close(); }
    }

    @Override
    public boolean update(Categorie o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try {
            s.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally { s.close(); }
    }

    @Override
    public boolean delete(Categorie o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try {
            s.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally { s.close(); }
    }

    @Override
    public Categorie getById(Long id) {
        Session s = getSession();
        try {
            return s.get(Categorie.class, id);
        } finally { s.close(); }
    }

    @Override
    public List<Categorie> getAll() {
        Session s = getSession();
        try {
            return s.createQuery("from Categorie", Categorie.class).list();
        } finally { s.close(); }
    }
}
