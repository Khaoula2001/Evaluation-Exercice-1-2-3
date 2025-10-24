package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class MariageService extends BaseService<Mariage> {
    public MariageService() { super(Mariage.class); }

    // Afficher les mariages d’un homme donné, avec détails
    public List<Mariage> mariagesDUnHomme(Long hommeId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            return s.createQuery("from Mariage m where m.homme.id = :hid order by m.dateDebut", Mariage.class)
                    .setParameter("hid", hommeId)
                    .getResultList();
        } finally { s.close(); }
    }

    public Homme chargerHomme(Long hommeId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try { return s.get(Homme.class, hommeId); } finally { s.close(); }
    }
}
