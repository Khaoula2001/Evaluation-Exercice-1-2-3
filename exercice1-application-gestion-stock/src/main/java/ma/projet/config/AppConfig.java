package ma.projet.config;

import ma.projet.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CategorieService categorieService(){
        return new CategorieService();
    }

    @Bean
    public ProduitService produitService(){
        return new ProduitService();
    }

    @Bean
    public CommandeService commandeService(){
        return new CommandeService();
    }

    @Bean
    public LigneCommandeService ligneCommandeService(){
        return new LigneCommandeService();
    }
}
