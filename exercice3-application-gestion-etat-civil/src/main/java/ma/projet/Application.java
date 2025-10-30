package ma.projet;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static Date d(String s){
        try { return new SimpleDateFormat("dd/MM/yyyy").parse(s); }
        catch (ParseException e) { throw new RuntimeException(e); }
    }

    @Bean
    CommandLineRunner runner(HommeService hommeService, FemmeService femmeService, MariageService mariageService){
        return args -> {
            if(femmeService.findAll().isEmpty() && hommeService.findAll().isEmpty()){
                // Créer 10 femmes
                List<Femme> femmes = new ArrayList<>();
                String[][] fdata = {
                        {"RAMI","SALIMA"},{"ALI","AMAL"},{"ALAOUI","WAFA"},{"ALAMI","KARIMA"},{"NASSER","HIBA"},
                        {"BOUCHRA","SARA"},{"KHALIL","NADA"},{"SABIR","MALIKA"},{"YASSIN","RANIA"},{"SAFI","HOUDA"}
                };
                for(int i=0;i<10;i++){
                    Femme f = new Femme();
                    f.setNom(fdata[i][0]); f.setPrenom(fdata[i][1]);
                    f.setDateNaissance(d("0"+(i+1)+"/01/1975"));
                    femmeService.save(f);
                    femmes.add(f);
                }
                // Créer 5 hommes
                List<Homme> hommes = new ArrayList<>();
                String[][] hdata = {{"SAFI","SAID"},{"MOURAD","OMAR"},{"HARTI","NABIL"},{"ZAHIR","IMAD"},{"KHALIL","YASSINE"}};
                for(int i=0;i<5;i++){
                    Homme h = new Homme();
                    h.setNom(hdata[i][0]); h.setPrenom(hdata[i][1]);
                    h.setDateNaissance(d("0"+(i+1)+"/01/1970"));
                    hommeService.save(h);
                    hommes.add(h);
                }
                // Mariages pour l'homme 1 (SAFI SAID)
                Homme said = hommes.get(0);
                Mariage m1 = new Mariage(); m1.setHomme(said); m1.setFemme(femmes.get(3)); m1.setDateDebut(d("03/09/1989")); m1.setDateFin(d("03/09/1990")); m1.setNbrEnfant(0); mariageService.save(m1);
                Mariage m2 = new Mariage(); m2.setHomme(said); m2.setFemme(femmes.get(0)); m2.setDateDebut(d("03/09/1990")); m2.setNbrEnfant(4); mariageService.save(m2);
                Mariage m3 = new Mariage(); m3.setHomme(said); m3.setFemme(femmes.get(1)); m3.setDateDebut(d("03/09/1995")); m3.setNbrEnfant(2); mariageService.save(m3);
                Mariage m4 = new Mariage(); m4.setHomme(said); m4.setFemme(femmes.get(2)); m4.setDateDebut(d("04/11/2000")); m4.setNbrEnfant(3); mariageService.save(m4);

                // Autres mariages pour atteindre divers cas
                Homme h2 = hommes.get(1);
                mariageService.save(nm(h2, femmes.get(4), "01/01/1999", null, 1));
                mariageService.save(nm(h2, femmes.get(5), "01/01/2001", null, 2));
                mariageService.save(nm(h2, femmes.get(6), "01/01/2003", null, 1));
                mariageService.save(nm(h2, femmes.get(7), "01/01/2005", null, 0));

                Homme h3 = hommes.get(2);
                mariageService.save(nm(h3, femmes.get(8), "01/06/2000", "01/01/2002", 0));
                mariageService.save(nm(h3, femmes.get(9), "01/06/2003", null, 2));
            }

            // Affichages demandés
            System.out.println("==== Liste des femmes ====");
            femmeService.findAll().forEach(f -> System.out.println(f.getNomComplet()));

            System.out.println("\n==== La femme la plus âgée ====");
            Femme oldest = femmeService.findAll().stream()
                    .min((a,b)->a.getDateNaissance().compareTo(b.getDateNaissance()))
                    .orElse(null);
            if(oldest!=null) System.out.println(oldest.getNomComplet());

            System.out.println("\n==== Épouses de SAFI SAID ====");
            Homme said = hommeService.findAll().stream().filter(h->"SAFI".equals(h.getNom()) && "SAID".equals(h.getPrenom())).findFirst().orElse(null);
            if(said!=null){
                hommeService.epousesEntreDates(said.getId(), d("01/01/1988"), d("31/12/2005")).forEach(f->System.out.println(f.getNomComplet()));
            }

            System.out.println("\n==== Nbr d'enfants de SALIMA RAMI entre 01/01/1989 et 31/12/1999 ====");
            Femme salima = femmeService.findAll().stream().filter(f->"RAMI".equals(f.getNom()) && "SALIMA".equals(f.getPrenom())).findFirst().orElse(null);
            if(salima!=null){
                long n = femmeService.compterEnfantsEntreDates(salima.getId(), d("01/01/1989"), d("31/12/1999"));
                System.out.println(n);
            }

            System.out.println("\n==== Femmes mariées au moins deux fois ====");
            femmeService.femmesMarieesAuMoinsDeuxFois().forEach(f->System.out.println(f.getNomComplet()));

            System.out.println("\n==== Hommes mariés à quatre femmes entre 1998 et 2006 (count) ====");
            int cnt = hommeService.nombreHommesMariesAQuatreFemmesEntreDates(d("01/01/1998"), d("31/12/2006"));
            System.out.println(cnt);

            System.out.println("\n==== Détails des mariages de SAFI SAID ====");
            if(said!=null){
                hommeService.afficherMariagesDetails(said.getId());
            }
        };
    }

    private static Mariage nm(Homme h, Femme f, String dd, String df, int n){
        Mariage m = new Mariage();
        m.setHomme(h); m.setFemme(f);
        m.setDateDebut(d(dd));
        if(df!=null) m.setDateFin(d(df));
        m.setNbrEnfant(n);
        return m;
    }
}
