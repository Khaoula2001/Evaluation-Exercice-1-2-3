package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FemmeService implements IDao<Femme> {

    @PersistenceContext
    private EntityManager em;

    private EntityManager session(){ return em; }

    // CRUD
    @Override
    public void save(Femme o) { session().persist(o); }

    @Override
    public void update(Femme o) { session().merge(o); }

    @Override
    public void delete(Femme o) { session().remove(session().contains(o) ? o : session().merge(o)); }

    @Override
    @Transactional(readOnly = true)
    public Femme findById(int id) { return session().find(Femme.class, id); }

    @Override
    @Transactional(readOnly = true)
    public List<Femme> findAll() {
        return session().createQuery("from Femme", Femme.class).getResultList();
    }

    // Métier
    @Transactional(readOnly = true)
    public long compterEnfantsEntreDates(int femmeId, Date d1, Date d2){
        Query q = session().createNamedQuery("Femme.countChildrenBetween");
        q.setParameter("femmeId", femmeId);
        q.setParameter("startDate", d1);
        q.setParameter("endDate", d2);
        Object o = q.getSingleResult();
        if(o instanceof Number) return ((Number)o).longValue();
        return Long.parseLong(o.toString());
    }

    @Transactional(readOnly = true)
    public List<Femme> femmesMarieesAuMoinsDeuxFois(){
        TypedQuery<Femme> q = session().createNamedQuery("Femme.marriedAtLeastTwice", Femme.class);
        return q.getResultList();
    }
}
