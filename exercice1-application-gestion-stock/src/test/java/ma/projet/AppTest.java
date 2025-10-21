package ma.projet;

import ma.projet.classes.*;
import ma.projet.service.*;
import ma.projet.util.HibernateUtil;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private static CategorieService categorieService;
    private static ProduitService produitService;
    private static CommandeService commandeService;
    private static LigneCommandeService ligneService;

    @BeforeAll
    static void init() {
        categorieService = new CategorieService();
        produitService = new ProduitService();
        commandeService = new CommandeService();
        ligneService = new LigneCommandeService();

        Categorie c1 = categorieService.save(new Categorie("CAT1", "Ordinateurs"));
        Categorie c2 = categorieService.save(new Categorie("CAT2", "Ecrans"));

        Produit p1 = produitService.save(new Produit("ES12", 120f, c1));
        Produit p2 = produitService.save(new Produit("ZR85", 100f, c1));
        Produit p3 = produitService.save(new Produit("EE85", 200f, c2));
        Produit p4 = produitService.save(new Produit("AA10", 80f, c2));

        Commande cmd1 = commandeService.save(new Commande(LocalDate.of(2013, 3, 14)));
        Commande cmd2 = commandeService.save(new Commande(LocalDate.of(2013, 4, 3)));

        ligneService.save(new LigneCommandeProduit(cmd1, p1, 7));
        ligneService.save(new LigneCommandeProduit(cmd1, p2, 14));
        ligneService.save(new LigneCommandeProduit(cmd1, p3, 5));
        ligneService.save(new LigneCommandeProduit(cmd2, p4, 2));
    }

    @AfterAll
    static void tearDownAll() {
        HibernateUtil.shutdown();
    }

    @Test
    void testListProduitsParCategorie() {
        Categorie cat1 = categorieService.findAll().stream().filter(c -> c.getCode().equals("CAT1")).findFirst().orElseThrow();
        List<Produit> list = produitService.listByCategorie(cat1);
        assertEquals(2, list.size());
    }

    @Test
    void testProduitsCommandesEntreDeuxDates() {
        List<Produit> list = produitService.findOrderedBetweenDates(LocalDate.of(2013, 3, 1), LocalDate.of(2013, 3, 31));
        assertTrue(list.stream().anyMatch(p -> p.getReference().equals("ES12")));
    }

    @Test
    void testProduitsParCommande() {
        Commande cmd = commandeService.findAll().get(0);
        List<Object[]> lignes = produitService.findByCommande(cmd);
        assertEquals(3, lignes.size());
        // Print expected display
        System.out.println("Commande : " + cmd.getId() + "     Date : " + cmd.getDate());
        System.out.println("Liste des produits :\nRéférence\tPrix\tQuantité");
        for (Object[] row : lignes) {
            System.out.println(row[0] + "\t" + row[1] + " DH\t" + row[2]);
        }
    }

    @Test
    void testProduitsPrixSup100NamedQuery() {
        List<Produit> list = produitService.findPrixSup100UsingNamedQuery();
        assertTrue(list.stream().allMatch(p -> p.getPrix() > 100));
    }
}
