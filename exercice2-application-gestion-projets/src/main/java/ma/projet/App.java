package ma.projet;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ma.projet.classes.*;
import ma.projet.service.*;

public class App {
    private static final SimpleDateFormat DF = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            // Services via contexte Spring (découplage)
            EmployeService employeService = ctx.getBean(EmployeService.class);
            ProjetService projetService = ctx.getBean(ProjetService.class);
            TacheService tacheService = ctx.getBean(TacheService.class);
            EmployeTacheService employeTacheService = ctx.getBean(EmployeTacheService.class);

            // Seed minimal data if DB empty
            if (projetService.getAll().isEmpty()) {
                seed(employeService, projetService, tacheService, employeTacheService);
            }

            // afficher un projet avec ses tâches réalisées (format demandé)
            List<Projet> projets = projetService.getAll();
            if (!projets.isEmpty()) {
                Projet p = projets.get(0);
                printProjetResume(p);
                System.out.println("Liste des tâches:");
                System.out.println("Num Nom            Date Début Réelle   Date Fin Réelle");
                for (EmployeTache et : projetService.listTachesRealiseesAvecDatesReelles(p.getId())) {
                    System.out.printf(Locale.FRANCE, "%d  %-14s  %-16s  %-16s%n",
                            et.getTache().getId(),
                            et.getTache().getNom(),
                            et.getDateDebutReelle() == null ? "" : DF.format(et.getDateDebutReelle()),
                            et.getDateFinReelle() == null ? "" : DF.format(et.getDateFinReelle()));
                }
            }

            // Autres exemples d'appels exigés par l'énoncé
            // - Tâches prix > 1000 DH
            List<Tache> cheres = tacheService.findTachesPrixSup(1000);
            System.out.println("\nTâches avec prix > 1000 DH: " + cheres.size());
            // - Tâches planifiées pour le 1er projet
            if (!projets.isEmpty()) {
                List<Tache> plan = projetService.listTachesPlanifieesPourProjet(projets.get(0).getId());
                System.out.println("Tâches planifiées: " + plan.size());
            }
        }
    }

    private static void printProjetResume(Projet p) {
        Calendar c = Calendar.getInstance(Locale.FRANCE);
        c.setTime(p.getDateDebut());
        String mois = new java.text.DateFormatSymbols(Locale.FRANCE).getMonths()[c.get(Calendar.MONTH)];
        System.out.println(String.format("Projet : %d      Nom : %s     Date début : %d %s %d",
                p.getId(), p.getNom(), c.get(Calendar.DAY_OF_MONTH), capitalize(mois), c.get(Calendar.YEAR)));
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static void seed(EmployeService es, ProjetService ps, TacheService ts, EmployeTacheService ets) {
        try {
            Date d1 = DF.parse("14/01/2013");
            Employe e1 = new Employe("Dupont", "Jean", "0600000000");
            es.create(e1);

            Projet p1 = new Projet("Gestion de stock", d1, DF.parse("14/07/2013"), e1);
            ps.create(p1);

            Tache t1 = new Tache("Analyse", DF.parse("10/02/2013"), DF.parse("20/02/2013"), 2000, p1);
            Tache t2 = new Tache("Conception", DF.parse("10/03/2013"), DF.parse("15/03/2013"), 1500, p1);
            Tache t3 = new Tache("Développement", DF.parse("10/04/2013"), DF.parse("25/04/2013"), 8000, p1);
            ts.create(t1); ts.create(t2); ts.create(t3);

            // Liaison + dates réelles = égales aux dates planifiées ici
            ets.create(new EmployeTache(e1, t1, t1.getDateDebut(), t1.getDateFin()));
            ets.create(new EmployeTache(e1, t2, t2.getDateDebut(), t2.getDateFin()));
            ets.create(new EmployeTache(e1, t3, t3.getDateDebut(), t3.getDateFin()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
