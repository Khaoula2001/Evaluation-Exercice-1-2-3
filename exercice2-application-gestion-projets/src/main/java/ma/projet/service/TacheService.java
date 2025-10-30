package ma.projet.service;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.projet.classes.Tache;
import ma.projet.dao.IDao;

@Service
public class TacheService implements IDao<Tache> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(Tache o) {
        Transaction tx = null;
        try (Session s = sessionFactory.openSession()) {
            tx = s.beginTransaction();
            s.save(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Tache getById(int id) {
        try (Session s = sessionFactory.openSession()) {
            return s.get(Tache.class, id);
        }
    }

    @Override
    public List<Tache> getAll() {
        try (Session s = sessionFactory.openSession()) {
            return s.createQuery("from Tache", Tache.class).list();
        }
    }

    @Override
    public boolean update(Tache o) {
        Transaction tx = null;
        try (Session s = sessionFactory.openSession()) {
            tx = s.beginTransaction();
            s.update(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Tache o) {
        Transaction tx = null;
        try (Session s = sessionFactory.openSession()) {
            tx = s.beginTransaction();
            s.delete(o);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    // Tâches dont le prix > min (utilise requête nommée)
    public List<Tache> findTachesPrixSup(double min) {
        try (Session s = sessionFactory.openSession()) {
            return s.createNamedQuery("Tache.findPrixSup", Tache.class)
                    .setParameter("minPrix", min)
                    .getResultList();
        }
    }

    // Tâches réalisées entre deux dates (ici sur dates planifiées fin)
    public List<Tache> findTachesRealiseesEntre(Date debut, Date fin) {
        try (Session s = sessionFactory.openSession()) {
            return s.createQuery(
                    "select t from Tache t where t.dateFin between :d1 and :d2",
                    Tache.class)
                    .setParameter("d1", debut)
                    .setParameter("d2", fin)
                    .getResultList();
        }
    }
}
