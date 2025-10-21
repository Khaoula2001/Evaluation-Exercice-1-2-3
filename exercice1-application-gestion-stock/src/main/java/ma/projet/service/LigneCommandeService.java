package ma.projet.service;

import ma.projet.classes.LigneCommandeProduit;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LigneCommandeService implements IDao<LigneCommandeProduit> {
    @Override
    public LigneCommandeProduit save(LigneCommandeProduit l) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            l = (LigneCommandeProduit) session.merge(l);
            tx.commit();
            return l;
        } catch (Exception e) { if (tx != null) tx.rollback(); throw e; }
    }

    @Override
    public LigneCommandeProduit update(LigneCommandeProduit l) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            l = (LigneCommandeProduit) session.merge(l);
            tx.commit();
            return l;
        } catch (Exception e) { if (tx != null) tx.rollback(); throw e; }
    }

    @Override
    public void delete(LigneCommandeProduit l) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(session.contains(l) ? l : session.merge(l));
            tx.commit();
        } catch (Exception e) { if (tx != null) tx.rollback(); throw e; }
    }

    @Override
    public LigneCommandeProduit findById(Long id) {
        throw new UnsupportedOperationException("Utilisez une clé composite via les champs de l'entité");
    }

    @Override
    public List<LigneCommandeProduit> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from LigneCommandeProduit", LigneCommandeProduit.class).getResultList();
        }
    }
}
