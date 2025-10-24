package ma.projet.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ma.projet.classes.Employe;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.dao.IDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EmployeService implements IDao<Employe> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Employe create(Employe o) {
        em.persist(o);
        return o;
    }

    @Override
    public Employe update(Employe o) {
        return em.merge(o);
    }

    @Override
    public void delete(Long id) {
        Employe e = em.find(Employe.class, id);
        if (e != null) em.remove(e);
    }

    @Override
    public Employe findById(Long id) {
        return em.find(Employe.class, id);
    }

    @Override
    public List<Employe> findAll() {
        return em.createQuery("from Employe", Employe.class).getResultList();
    }

    // Afficher la liste des tâches réalisées par un employé (via EmployeTache)
    public List<EmployeTache> tachesRealiseesParEmploye(Long employeId) {
        return em.createQuery(
                "select et from EmployeTache et where et.employe.id = :id and et.dateDebutReelle is not null",
                EmployeTache.class)
            .setParameter("id", employeId)
            .getResultList();
    }

    // Afficher la liste des projets gérés par un employé (chef de projet)
    public List<Projet> projetsGeresParEmploye(Long employeId) {
        return em.createQuery(
                "select p from Projet p where p.chefProjet.id = :id",
                Projet.class)
            .setParameter("id", employeId)
            .getResultList();
    }
}
