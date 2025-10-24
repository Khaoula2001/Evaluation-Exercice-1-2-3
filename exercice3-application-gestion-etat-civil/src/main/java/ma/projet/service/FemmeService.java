package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

public class FemmeService extends BaseService<Femme> {
    public FemmeService() { super(Femme.class); }

    // Requête native nommée: nombre d’enfants d’une femme entre deux dates.
    public int nombreEnfantsEntreDates(Long femmeId, LocalDate start, LocalDate end) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            NativeQuery<?> q = (NativeQuery<?>) s.createNativeQuery("SELECT COALESCE(SUM(m.nbr_enfant),0) FROM mariages m WHERE m.femme_id = :femmeId AND m.date_debut >= :start AND (m.date_fin IS NULL OR m.date_fin <= :end)");
            q.setParameter("femmeId", femmeId);
            q.setParameter("start", start);
            q.setParameter("end", end);
            Object res = q.uniqueResult();
            if (res == null) return 0;
            if (res instanceof Number) return ((Number) res).intValue();
            return Integer.parseInt(res.toString());
        } finally { s.close(); }
    }

    // Requête nommée: femmes mariées au moins deux fois
    @SuppressWarnings("unchecked")
    public List<Femme> femmesMarieesAuMoinsDeuxFois() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try { return s.createNamedQuery("Femme.marriedAtLeastTwice").getResultList(); }
        finally { s.close(); }
    }

    // API Criteria: nombre d’hommes mariés à quatre femmes entre deux dates
    public long nombreHommesMarieAQuatreFemmesEntre(LocalDate start, LocalDate end) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            CriteriaBuilder cb = s.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Homme> homme = cq.from(Homme.class);
            Join<Homme, Mariage> mariage = homme.join("mariages");
            cq.select(homme.get("id"));
            cq.where(
                    cb.and(
                            cb.greaterThanOrEqualTo(mariage.get("dateDebut"), start),
                            cb.or(cb.isNull(mariage.get("dateFin")), cb.lessThanOrEqualTo(mariage.get("dateFin"), end))
                    )
            );
            cq.groupBy(homme.get("id"));
            cq.having(cb.greaterThanOrEqualTo(cb.countDistinct(mariage.get("femme")), 4L));
            List<Long> ids = s.createQuery(cq).getResultList();
            return ids.size();
        } finally { s.close(); }
    }
}
