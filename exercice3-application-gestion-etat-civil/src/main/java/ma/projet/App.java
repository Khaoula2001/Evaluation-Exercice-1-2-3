package ma.projet;

import ma.projet.beans.*;
import ma.projet.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class App {
    public static void main(String[] args) {
        HommeService hommeService = new HommeService();
        FemmeService femmeService = new FemmeService();
        MariageService mariageService = new MariageService();

        // Seed data only if DB is empty
        if (femmeService.findAll().isEmpty() && hommeService.findAll().isEmpty()) {
            seedData(hommeService, femmeService, mariageService);
        }

        // 1. Afficher la liste des femmes
        System.out.println("Liste des femmes :");
        femmeService.findAll().forEach(f -> System.out.println("- " + f.getNomComplet()));

        // 2. Afficher la femme la plus âgée
        Femme plusAgee = femmeService.findAll().stream()
                .min(Comparator.comparing(Femme::getDateNaissance))
                .orElse(null);
        System.out.println("Femme la plus âgée : " + (plusAgee != null ? plusAgee.getNomComplet() : "N/A"));

        // Pick a sample man (first one)
        List<Homme> hommes = hommeService.findAll();
        if (!hommes.isEmpty()) {
            Homme h = hommes.get(0);
            LocalDate start = LocalDate.of(1980, 1, 1);
            LocalDate end = LocalDate.of(2030, 12, 31);

            // 3. Afficher les épouses d’un homme donné
            System.out.println("Epouses de l'homme " + h.getNomComplet() + " entre " + start + " et " + end + ":");
            hommeService.epousesEntreDates(h.getId(), start, end).forEach(f -> System.out.println("- " + f.getNomComplet()));

            // 4. Afficher le nombre d’enfants d’une femme entre deux dates (use first woman if exists)
            List<Femme> femmes = femmeService.findAll();
            if (!femmes.isEmpty()) {
                Femme f1 = femmes.get(0);
                int n = femmeService.nombreEnfantsEntreDates(f1.getId(), start, end);
                System.out.println("Nombre d'enfants de " + f1.getNomComplet() + " entre " + start + " et " + end + " = " + n);
            }

            // 5. Afficher les femmes mariées deux fois ou plus
            System.out.println("Femmes mariées au moins deux fois:");
            femmeService.femmesMarieesAuMoinsDeuxFois().forEach(f -> System.out.println("- " + f.getNomComplet()));

            // 6. Afficher les hommes mariés à quatre femmes entre deux dates (compte)
            long c = femmeService.nombreHommesMarieAQuatreFemmesEntre(start, end);
            System.out.println("Nombre d'hommes mariés à 4 femmes entre " + start + " et " + end + " = " + c);

            // 7. Afficher les mariages d’un homme avec tous les détails.
            afficherMariagesHomme(h, mariageService);
        }
    }

    private static void afficherMariagesHomme(Homme homme, MariageService mariageService) {
        List<Mariage> mariages = mariageService.mariagesDUnHomme(homme.getId());
        System.out.println("\nNom : " + homme.getNomComplet().toUpperCase());
        List<Mariage> enCours = new ArrayList<>();
        List<Mariage> echoues = new ArrayList<>();
        for (Mariage m : mariages) { if (m.isEnCours()) enCours.add(m); else echoues.add(m); }

        System.out.println("Mariages En Cours :");
        int i = 1;
        for (Mariage m : enCours) {
            System.out.printf("%d. Femme : %s   Date Début : %s    Nbr Enfants : %d%n",
                    i++, m.getFemme().getNomComplet().toUpperCase(), m.getDateDebut(), m.getNbrEnfant());
        }
        System.out.println("\nMariages échoués :");
        i = 1;
        for (Mariage m : echoues) {
            System.out.printf("%d. Femme : %s  Date Début : %s    Date Fin : %s    Nbr Enfants : %d%n",
                    i++, m.getFemme().getNomComplet().toUpperCase(), m.getDateDebut(), m.getDateFin(), m.getNbrEnfant());
        }
    }

    private static void seedData(HommeService hommeService, FemmeService femmeService, MariageService mariageService) {
        // Create 5 hommes
        Homme h1 = homme("SAFI", "SAID", LocalDate.of(1960, 3, 9));
        Homme h2 = homme("EL", "AMINE", LocalDate.of(1965, 5, 12));
        Homme h3 = homme("BOU", "YASSINE", LocalDate.of(1970, 7, 21));
        Homme h4 = homme("KHAL", "OMAR", LocalDate.of(1975, 1, 2));
        Homme h5 = homme("NAD", "ANAS", LocalDate.of(1980, 9, 14));
        h1 = hommeService.create(h1); h2 = hommeService.create(h2); h3 = hommeService.create(h3); h4 = hommeService.create(h4); h5 = hommeService.create(h5);

        // Create 10 femmes
        Femme f1 = femme("SALIMA", "RAMI", LocalDate.of(1968, 2, 1));
        Femme f2 = femme("AMAL", "ALI", LocalDate.of(1971, 4, 3));
        Femme f3 = femme("WAFA", "ALAOUI", LocalDate.of(1975, 11, 4));
        Femme f4 = femme("KARIMA", "ALAMI", LocalDate.of(1969, 9, 9));
        Femme f5 = femme("HIND", "RAZI", LocalDate.of(1972, 3, 15));
        Femme f6 = femme("NAJAT", "SAMI", LocalDate.of(1976, 6, 18));
        Femme f7 = femme("SARA", "BEN", LocalDate.of(1980, 1, 1));
        Femme f8 = femme("ILHAM", "ZAKI", LocalDate.of(1982, 12, 12));
        Femme f9 = femme("LINA", "JABRI", LocalDate.of(1978, 8, 23));
        Femme f10 = femme("DALIA", "AMRANI", LocalDate.of(1977, 10, 30));
        Femme[] arr = {f1,f2,f3,f4,f5,f6,f7,f8,f9,f10};
        for (int i=0;i<arr.length;i++) arr[i]=femmeService.create(arr[i]);

        // Mariages for h1 (3 en cours, 1 échoué per example)
        mariageService.create(mariage(h1, f4, LocalDate.of(1989,9,3), LocalDate.of(1990,9,3), 0));
        mariageService.create(mariage(h1, f1, LocalDate.of(1990,9,3), null, 4));
        mariageService.create(mariage(h1, f2, LocalDate.of(1995,9,3), null, 2));
        mariageService.create(mariage(h1, f3, LocalDate.of(2000,11,4), null, 3));

        // Marriages for other men to create variety, including one with 4 wives between dates
        mariageService.create(mariage(h2, f5, LocalDate.of(1992,5,1), null, 1));
        mariageService.create(mariage(h2, f6, LocalDate.of(1994,6,1), null, 2));
        mariageService.create(mariage(h2, f7, LocalDate.of(1996,7,1), null, 0));
        mariageService.create(mariage(h2, f8, LocalDate.of(1998,8,1), null, 1));

        mariageService.create(mariage(h3, f9, LocalDate.of(2001,2,2), null, 2));
        mariageService.create(mariage(h4, f10, LocalDate.of(2003,3,3), LocalDate.of(2010,1,1), 1));
        mariageService.create(mariage(h5, f5, LocalDate.of(2010,4,4), null, 3));

        // Make at least one woman married twice
        mariageService.create(mariage(h3, f5, LocalDate.of(2005,1,1), null, 0));
    }

    private static Homme homme(String nom, String prenom, LocalDate naissance) {
        Homme h = new Homme();
        h.setNom(nom); h.setPrenom(prenom); h.setDateNaissance(naissance);
        h.setAdresse("adresse"); h.setTelephone("000000");
        return h;
    }

    private static Femme femme(String nom, String prenom, LocalDate naissance) {
        Femme f = new Femme();
        f.setNom(nom); f.setPrenom(prenom); f.setDateNaissance(naissance);
        f.setAdresse("adresse"); f.setTelephone("000000");
        return f;
    }

    private static Mariage mariage(Homme h, Femme f, LocalDate start, LocalDate end, int enfants) {
        Mariage m = new Mariage();
        m.setHomme(h); m.setFemme(f); m.setDateDebut(start); m.setDateFin(end); m.setNbrEnfant(enfants);
        return m;
    }
}
