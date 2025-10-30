package ma.projet;

import ma.projet.classes.*;
import ma.projet.config.AppConfig;
import ma.projet.service.*;
import org.junit.jupiter.api.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProduitServiceTest {
    private static ApplicationContext ctx;
    private static CategorieService categorieService;
    private static ProduitService produitService;
    private static CommandeService commandeService;
    private static LigneCommandeService ligneService;

    @BeforeAll
    public static void init(){
        ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        categorieService = ctx.getBean(CategorieService.class);
        produitService = ctx.getBean(ProduitService.class);
        commandeService = ctx.getBean(CommandeService.class);
        ligneService = ctx.getBean(LigneCommandeService.class);
    }

    @Test @Order(1)
    public void seedData(){
        if (categorieService.getAll().isEmpty()) {
            Categorie c1 = new Categorie("TST-1", "Test 1");
            Categorie c2 = new Categorie("TST-2", "Test 2");
            categorieService.create(c1);
            categorieService.create(c2);

            Produit p1 = new Produit("P-TST-1", 150f); p1.setCategorie(c1); produitService.create(p1);
            Produit p2 = new Produit("P-TST-2", 90f);  p2.setCategorie(c1); produitService.create(p2);
            Produit p3 = new Produit("P-TST-3", 200f); p3.setCategorie(c2); produitService.create(p3);

            Commande cmd = new Commande(LocalDate.now());
            commandeService.create(cmd);
            ligneService.create(new LigneCommandeProduit(cmd, p1, 3));
            ligneService.create(new LigneCommandeProduit(cmd, p3, 5));
        }
        Assertions.assertFalse(categorieService.getAll().isEmpty());
        Assertions.assertFalse(produitService.getAll().isEmpty());
        Assertions.assertFalse(commandeService.getAll().isEmpty());
    }

    @Test @Order(2)
    public void testProduitsParCategorie(){
        Categorie c = categorieService.getAll().get(0);
        List<Produit> produits = produitService.getByCategorie(c);
        Assertions.assertNotNull(produits);
    }

    @Test @Order(3)
    public void testProduitsEntreDeuxDates(){
        LocalDate d1 = LocalDate.now().minusDays(1);
        LocalDate d2 = LocalDate.now().plusDays(1);
        List<Produit> produits = produitService.getCommandesBetween(d1, d2);
        Assertions.assertNotNull(produits);
    }

    @Test @Order(4)
    public void testProduitsByCommande(){
        Commande cmd = commandeService.getAll().get(0);
        List<Object[]> rows = produitService.getProduitDetailsByCommande(cmd.getId());
        Assertions.assertNotNull(rows);
    }

    @Test @Order(5)
    public void testPrixSuperieur(){
        List<Produit> produits = produitService.getPrixSuperieur(100f);
        for (Produit p : produits) {
            Assertions.assertTrue(p.getPrix() > 100f);
        }
    }
}
