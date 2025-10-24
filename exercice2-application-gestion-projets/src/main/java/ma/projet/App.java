package ma.projet;

import ma.projet.classes.*;
import ma.projet.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    CommandLineRunner demo(EmployeService employeService,
                           ProjetService projetService,
                           TacheService tacheService,
                           EmployeTacheService employeTacheService) {
        return args -> {
            // Création des employés
            Employe chef = employeService.create(new Employe("Dupont", "Alice", "0600000001"));
            Employe dev1 = employeService.create(new Employe("Martin", "Bob", "0600000002"));
            Employe dev2 = employeService.create(new Employe("Durand", "Chloe", "0600000003"));

            // Création d'un projet
            Projet projet = new Projet("Gestion de stock",
                    LocalDate.of(2013, 1, 14),
                    LocalDate.of(2013, 12, 31),
                    chef);
            projet = projetService.create(projet);

            // Tâches planifiées
            Tache t1 = tacheService.create(new Tache("Analyse", LocalDate.of(2013,2,10), LocalDate.of(2013,2,20), 1500, projet));
            Tache t2 = tacheService.create(new Tache("Conception", LocalDate.of(2013,3,10), LocalDate.of(2013,3,15), 1800, projet));
            Tache t3 = tacheService.create(new Tache("Développement", LocalDate.of(2013,4,10), LocalDate.of(2013,4,25), 5000, projet));

            // Affectations réelles
            employeTacheService.create(new EmployeTache(dev1, t1, LocalDate.of(2013,2,10), LocalDate.of(2013,2,20)));
            employeTacheService.create(new EmployeTache(dev1, t2, LocalDate.of(2013,3,10), LocalDate.of(2013,3,15)));
            employeTacheService.create(new EmployeTache(dev2, t3, LocalDate.of(2013,4,10), LocalDate.of(2013,4,25)));

            // Affichages demandés
            printProjet(projet, projetService);
            System.out.println();

            // Exemples d'utilisation des autres méthodes
            List<Tache> chères = tacheService.tachesPrixSuperieur(1000);
            System.out.println("Tâches avec prix > 1000 DH: ");
            chères.forEach(t -> System.out.println(" - " + t.getNom() + " (" + t.getPrix() + ")"));

            System.out.println();
            System.out.println("Tâches réalisées entre 01/03/2013 et 30/04/2013:");
            employeTacheService.findAll().stream()
                    .filter(et -> et.getDateDebutReelle()!=null)
                    .filter(et -> !et.getDateDebutReelle().isBefore(LocalDate.of(2013,3,1))
                               && !et.getDateFinReelle().isAfter(LocalDate.of(2013,4,30)))
                    .forEach(et -> System.out.println(" - " + et.getTache().getNom()));

            System.out.println();
            System.out.println("Projets gérés par " + chef.getNom() + " :");
            employeService.projetsGeresParEmploye(chef.getId()).forEach(p -> System.out.println(" - " + p.getNom()));
        };
    }

    private static void printProjet(Projet projet, ProjetService projetService) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("d MMMM uuuu");
        System.out.println("Projet : " + projet.getId() + "      Nom : " + projet.getNom() + "     Date début : " + fmt.format(projet.getDateDebut()));
        System.out.println("Liste des tâches:");
        System.out.println("Num Nom            Date Début Réelle   Date Fin Réelle");
        List<EmployeTache> realisations = projetService.tachesRealiseesAvecDates(projet.getId());
        for (EmployeTache et : realisations) {
            String id = String.valueOf(et.getTache().getId());
            String nom = String.format("%-14s", et.getTache().getNom());
            String dd = et.getDateDebutReelle()!=null ? et.getDateDebutReelle().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")) : "";
            String df = et.getDateFinReelle()!=null ? et.getDateFinReelle().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")) : "";
            System.out.println(String.format("%s  %s %s          %s", id, nom, dd, df));
        }
    }
}
