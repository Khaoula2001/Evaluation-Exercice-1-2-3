package ma.projet;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.config.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Spring context
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        CategorieService categorieService = ctx.getBean(CategorieService.class);
        ProduitService produitService = ctx.getBean(ProduitService.class);
        CommandeService commandeService = ctx.getBean(CommandeService.class);
        LigneCommandeService ligneService = ctx.getBean(LigneCommandeService.class);

        if (categorieService.getAll().isEmpty()) {
            Categorie c1 = new Categorie("CAT-EL", "Electronique");
            Categorie c2 = new Categorie("CAT-ACC", "Accessoires");
            categorieService.create(c1);
            categorieService.create(c2);

            Produit p1 = new Produit("ES12", 120f); p1.setCategorie(c1); produitService.create(p1);
            Produit p2 = new Produit("ZR85", 100f); p2.setCategorie(c1); produitService.create(p2);
            Produit p3 = new Produit("EE85", 200f); p3.setCategorie(c2); produitService.create(p3);
            Produit p4 = new Produit("AA10", 80f);  p4.setCategorie(c2); produitService.create(p4);

            Commande cmd = new Commande(LocalDate.of(2013, 3, 14));
            commandeService.create(cmd);

            // Create lines (commande and produits must be already persisted to have IDs)
            ligneService.create(new LigneCommandeProduit(cmd, p1, 7));
            ligneService.create(new LigneCommandeProduit(cmd, p2, 14));
            ligneService.create(new LigneCommandeProduit(cmd, p3, 5));
        }

        // Fetch first commande for demo
        List<Commande> commandes = commandeService.getAll();
        if (!commandes.isEmpty()) {
            Commande cmd = commandes.get(0);
            System.out.println("Commande : " + cmd.getId() + "     Date : " + cmd.getDate());
            System.out.println("Liste des produits :");
            System.out.println("Référence\tPrix\tQuantité");
            for (Object[] row : produitService.getProduitDetailsByCommande(cmd.getId())) {
                String ref = (String) row[0];
                Float prix = (Float) row[1];
                Integer qte = (Integer) row[2];
                System.out.println(ref + "\t" + prix + " DH\t" + qte);
            }
        }

        // Display products with prix > 100 using named query
        System.out.println("\nProduits avec prix > 100 DH :");
        for (Produit p : produitService.getPrixSuperieur(100f)) {
            System.out.println(p.getReference() + " - " + p.getPrix() + " DH");
        }
    }
}
