package ma.projet;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ma.projet.util.HibernateUtil;

@Configuration
@ComponentScan(basePackages = "ma.projet")
public class AppConfig {

    @Bean
    public SessionFactory sessionFactory() {
        // Délègue à HibernateUtil pour respecter l'instruction de créer cette classe
        return HibernateUtil.getSessionFactory();
    }
}
