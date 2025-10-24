package ma.projet.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ma.projet.classes.EmployeTache;
import ma.projet.classes.Tache;
import ma.projet.dao.IDao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TacheService implements IDao<Tache> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Tache create(Tache o) {
        em.persist(o);
        return o;
    }

    @Override
    public Tache update(Tache o) {
        return em.merge(o);
    }

    @Override
    public void delete(Long id) {
        Tache t = em.find(Tache.class, id);
        if (t != null) em.remove(t);
    }

    @Override
    public Tache findById(Long id) {
        return em.find(Tache.class, id);
    }

    @Override
    public List<Tache> findAll() {
        return em.createQuery("from Tache", Tache.class).getResultList();
    }

    // Méthode pour afficher les tâches dont le prix est supérieur à 'min' (requête nommée)
    public List<Tache> tachesPrixSuperieur(double min) {
        return em.createNamedQuery("Tache.findByPrixSup", Tache.class)
                .setParameter("min", min)
                .getResultList();
    }

    // Méthode pour afficher les tâches réalisées entre deux dates (basée sur dates réelles dans EmployeTache)
    public List<EmployeTache> tachesRealiseesEntre(LocalDate debut, LocalDate fin) {
        return em.createQuery(
                "select et from EmployeTache et where et.dateDebutReelle >= :d1 and et.dateFinReelle <= :d2",
                EmployeTache.class)
            .setParameter("d1", debut)
            .setParameter("d2", fin)
            .getResultList();
    }
}
