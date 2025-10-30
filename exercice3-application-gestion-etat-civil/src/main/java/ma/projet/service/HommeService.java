package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class HommeService implements IDao<Homme> {

    @PersistenceContext
    private EntityManager em;

    private EntityManager session(){ return em; }

    // CRUD
    @Override
    public void save(Homme o) { session().persist(o); }

    @Override
    public void update(Homme o) { session().merge(o); }

    @Override
    public void delete(Homme o) { session().remove(session().contains(o) ? o : session().merge(o)); }

    @Override
    @Transactional(readOnly = true)
    public Homme findById(int id) { return session().find(Homme.class, id); }

    @Override
    @Transactional(readOnly = true)
    public List<Homme> findAll() { return session().createQuery("from Homme", Homme.class).getResultList(); }

    // Métier
    @Transactional(readOnly = true)
    public List<Femme> epousesEntreDates(int hommeId, Date d1, Date d2){
        String hql = "select distinct m.femme from Mariage m where m.homme.id = :hid and m.dateDebut between :d1 and :d2";
        return session().createQuery(hql, Femme.class)
                .setParameter("hid", hommeId)
                .setParameter("d1", d1)
                .setParameter("d2", d2)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public int nombreHommesMariesAQuatreFemmesEntreDates(Date d1, Date d2){
        CriteriaBuilder cb = session().getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Mariage> m = cq.from(Mariage.class);
        cq.select(cb.countDistinct(m.get("homme")));
        cq.where(cb.between(m.get("dateDebut"), d1, d2));
        cq.groupBy(m.get("homme"));
        cq.having(cb.ge(cb.count(m.get("femme")), 4L));
        List<Long> res = session().createQuery(cq).getResultList();
        return res.size();
    }

    @Transactional(readOnly = true)
    public List<Mariage> mariagesAvecDetails(int hommeId){
        String hql = "from Mariage m join fetch m.femme where m.homme.id = :hid order by m.dateDebut";
        return session().createQuery(hql, Mariage.class)
                .setParameter("hid", hommeId)
                .getResultList();
    }

    public void afficherMariagesDetails(int hommeId){
        Homme h = findById(hommeId);
        if(h==null){
            System.out.println("Homme introuvable: " + hommeId);
            return;
        }
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Nom : " + h.getNomComplet());
        List<Mariage> all = mariagesAvecDetails(hommeId);
        System.out.println("Mariages En Cours :");
        int i=1;
        for(Mariage m : all){
            if(m.getDateFin()==null){
                System.out.println(i+". Femme : " + m.getFemme().getNomComplet() +
                        "   Date Début : " + df.format(m.getDateDebut()) +
                        "    Nbr Enfants : " + m.getNbrEnfant());
                i++;
            }
        }
        System.out.println();
        System.out.println("Mariages échoués :");
        i=1;
        for(Mariage m : all){
            if(m.getDateFin()!=null){
                System.out.println(i+". Femme : " + m.getFemme().getNomComplet() +
                        "  Date Début : " + df.format(m.getDateDebut()) +
                        "    \nDate Fin : " + df.format(m.getDateFin()) +
                        "    Nbr Enfants : " + m.getNbrEnfant());
                i++;
            }
        }
    }
}
