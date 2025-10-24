package ma.projet.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ma.projet.classes.EmployeTache;
import ma.projet.dao.IDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class EmployeTacheService implements IDao<EmployeTache> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public EmployeTache create(EmployeTache o) {
        em.persist(o);
        return o;
    }

    @Override
    public EmployeTache update(EmployeTache o) {
        return em.merge(o);
    }

    @Override
    public void delete(Long id) {
        // EmployeTache utilise une clé composite, cette méthode ne sera pas utilisée dans ce TP
        throw new UnsupportedOperationException("Suppression par id simple non supportée pour EmployeTache");
    }

    public void delete(EmployeTache entity) {
        EmployeTache managed = em.merge(entity);
        em.remove(managed);
    }

    @Override
    public EmployeTache findById(Long id) {
        // Non applicable avec clé composite
        return null;
    }

    public EmployeTache findByIds(Long employeId, Long tacheId) {
        return em.find(EmployeTache.class, new ma.projet.classes.EmployeTacheId(employeId, tacheId));
    }

    @Override
    public List<EmployeTache> findAll() {
        return em.createQuery("from EmployeTache", EmployeTache.class).getResultList();
    }
}
