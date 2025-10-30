package ma.projet.service;

import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class MariageService implements IDao<Mariage> {
    @PersistenceContext
    private EntityManager em;

    private EntityManager session(){ return em; }

    @Override
    public void save(Mariage o) { session().persist(o); }

    @Override
    public void update(Mariage o) { session().merge(o); }

    @Override
    public void delete(Mariage o) { session().remove(session().contains(o) ? o : session().merge(o)); }

    @Override
    @Transactional(readOnly = true)
    public Mariage findById(int id) { return session().find(Mariage.class, id); }

    @Override
    @Transactional(readOnly = true)
    public List<Mariage> findAll() { return session().createQuery("from Mariage", Mariage.class).getResultList(); }
}
