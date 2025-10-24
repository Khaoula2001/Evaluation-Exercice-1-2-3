package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HommeService extends BaseService<Homme> {
    public HommeService() { super(Homme.class); }

    // Afficher les épouses d’un homme entre deux dates
    public List<Femme> epousesEntreDates(Long hommeId, LocalDate start, LocalDate end) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            Homme h = s.get(Homme.class, hommeId);
            if (h == null) return List.of();
            return h.getMariages().stream()
                    .filter(m -> !m.getDateDebut().isBefore(start) && (m.getDateFin() == null || !m.getDateFin().isAfter(end)))
                    .map(Mariage::getFemme)
                    .collect(Collectors.toList());
        } finally {
            s.close();
        }
    }
}
