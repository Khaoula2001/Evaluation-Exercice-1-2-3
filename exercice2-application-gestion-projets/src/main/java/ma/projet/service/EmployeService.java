package ma.projet.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.dao.IDao;

@Service
public class EmployeService implements IDao<Employe> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean create(Employe o) {
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
    public Employe getById(int id) {
        try (Session s = sessionFactory.openSession()) {
            return s.get(Employe.class, id);
        }
    }

    @Override
    public List<Employe> getAll() {
        try (Session s = sessionFactory.openSession()) {
            return s.createQuery("from Employe", Employe.class).list();
        }
    }

    @Override
    public boolean update(Employe o) {
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
    public boolean delete(Employe o) {
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

    // Afficher la liste des tâches réalisées par un employé (dates réelles non nulles)
    public List<EmployeTache> listTachesRealiseesParEmploye(int employeId) {
        try (Session s = sessionFactory.openSession()) {
            return s.createQuery(
                    "select et from EmployeTache et where et.employe.id = :emp and et.dateDebutReelle is not null",
                    EmployeTache.class)
                    .setParameter("emp", employeId)
                    .getResultList();
        }
    }

    // Afficher la liste des projets gérés par un employé (chef de projet)
    public List<Projet> listProjetsGeresParEmploye(int employeId) {
        try (Session s = sessionFactory.openSession()) {
            return s.createQuery(
                    "select p from Projet p where p.chef.id = :emp",
                    Projet.class)
                    .setParameter("emp", employeId)
                    .getResultList();
        }
    }
}
