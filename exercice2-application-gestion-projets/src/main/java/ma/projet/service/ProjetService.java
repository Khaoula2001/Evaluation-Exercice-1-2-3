package ma.projet.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;

@Service
public class ProjetService implements IDao<Projet> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(Projet o) {
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
    public Projet getById(int id) {
        try (Session s = sessionFactory.openSession()) {
            return s.get(Projet.class, id);
        }
    }

    @Override
    public List<Projet> getAll() {
        try (Session s = sessionFactory.openSession()) {
            return s.createQuery("from Projet", Projet.class).list();
        }
    }

    @Override
    public boolean update(Projet o) {
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
    public boolean delete(Projet o) {
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

    // Afficher la liste des tâches planifiées pour un projet
    public List<Tache> listTachesPlanifieesPourProjet(int projetId) {
        try (Session s = sessionFactory.openSession()) {
            return s.createQuery("select t from Tache t where t.projet.id = :p order by t.dateDebut", Tache.class)
                    .setParameter("p", projetId)
                    .getResultList();
        }
    }

    // Afficher la liste des tâches réalisées avec les dates réelles
    public List<EmployeTache> listTachesRealiseesAvecDatesReelles(int projetId) {
        try (Session s = sessionFactory.openSession()) {
            return s.createQuery(
                    "select et from EmployeTache et where et.tache.projet.id = :p and et.dateDebutReelle is not null",
                    EmployeTache.class)
                    .setParameter("p", projetId)
                    .getResultList();
        }
    }
}
