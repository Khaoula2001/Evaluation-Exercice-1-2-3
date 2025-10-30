package ma.projet.service;

import ma.projet.classes.LigneCommandeProduit;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.util.HibernateUtil;

import java.util.List;

public class LigneCommandeService implements IDao<LigneCommandeProduit, LigneCommandeProduit.PK> {
    private Session getSession(){
        return HibernateUtil.getSessionFactory().openSession();
    }
    @Override
    public boolean create(LigneCommandeProduit o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try {
            s.save(o);
            tx.commit();
            return true;
        } catch (Exception e){ tx.rollback(); throw e; }
        finally { s.close(); }
    }

    @Override
    public boolean update(LigneCommandeProduit o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try {
            s.update(o);
            tx.commit();
            return true;
        } catch (Exception e){ tx.rollback(); throw e; }
        finally { s.close(); }
    }

    @Override
    public boolean delete(LigneCommandeProduit o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try {
            s.delete(o);
            tx.commit();
            return true;
        } catch (Exception e){ tx.rollback(); throw e; }
        finally { s.close(); }
    }

    @Override
    public LigneCommandeProduit getById(LigneCommandeProduit.PK id) {
        Session s = getSession();
        try { return s.get(LigneCommandeProduit.class, id); }
        finally { s.close(); }
    }

    @Override
    public List<LigneCommandeProduit> getAll() {
        Session s = getSession();
        try { return s.createQuery("from LigneCommandeProduit", LigneCommandeProduit.class).list(); }
        finally { s.close(); }
    }
}
