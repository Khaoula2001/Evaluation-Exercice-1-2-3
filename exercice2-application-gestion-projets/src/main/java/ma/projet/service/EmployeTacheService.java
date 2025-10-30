package ma.projet.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.projet.classes.EmployeTache;
import ma.projet.dao.IDao;

@Service
public class EmployeTacheService implements IDao<EmployeTache> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(EmployeTache o) {
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
    public EmployeTache getById(int id) {
        try (Session s = sessionFactory.openSession()) {
            return s.get(EmployeTache.class, id);
        }
    }

    @Override
    public List<EmployeTache> getAll() {
        try (Session s = sessionFactory.openSession()) {
            return s.createQuery("from EmployeTache", EmployeTache.class).list();
        }
    }

    @Override
    public boolean update(EmployeTache o) {
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
    public boolean delete(EmployeTache o) {
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

    public List<EmployeTache> listByEmploye(int employeId) {
        try (Session s = sessionFactory.openSession()) {
            return s.createQuery("from EmployeTache et where et.employe.id = :e", EmployeTache.class)
                    .setParameter("e", employeId)
                    .getResultList();
        }
    }

    public List<EmployeTache> listByProjet(int projetId) {
        try (Session s = sessionFactory.openSession()) {
            return s.createQuery("from EmployeTache et where et.tache.projet.id = :p", EmployeTache.class)
                    .setParameter("p", projetId)
                    .getResultList();
        }
    }
}
