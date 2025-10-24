package ma.projet.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProjetService implements IDao<Projet> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Projet create(Projet o) {
        em.persist(o);
        return o;
    }

    @Override
    public Projet update(Projet o) {
        return em.merge(o);
    }

    @Override
    public void delete(Long id) {
        Projet p = em.find(Projet.class, id);
        if (p != null) em.remove(p);
    }

    @Override
    public Projet findById(Long id) {
        return em.find(Projet.class, id);
    }

    @Override
    public List<Projet> findAll() {
        return em.createQuery("from Projet", Projet.class).getResultList();
    }

    // Afficher la liste des tâches planifiées pour un projet
    public List<Tache> tachesPlanifiees(Long projetId) {
        return em.createQuery("select t from Tache t where t.projet.id = :id order by t.dateDebut", Tache.class)
                .setParameter("id", projetId)
                .getResultList();
    }

    // Afficher la liste des tâches réalisées avec les dates réelles
    public List<EmployeTache> tachesRealiseesAvecDates(Long projetId) {
        return em.createQuery(
                "select et from EmployeTache et where et.tache.projet.id = :id and et.dateDebutReelle is not null",
                EmployeTache.class)
            .setParameter("id", projetId)
            .getResultList();
    }
}
