package ma.projet.util;

import java.io.IOException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.classes.EmployeTache;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties props = new Properties();
                try {
                    props.load(HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties"));
                } catch (IOException e) {
                    throw new RuntimeException("Impossible de charger application.properties", e);
                }

                configuration.setProperty(Environment.DIALECT, props.getProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"));
                configuration.setProperty(Environment.SHOW_SQL, props.getProperty("hibernate.show_sql", "true"));
                configuration.setProperty(Environment.FORMAT_SQL, props.getProperty("hibernate.format_sql", "true"));
                configuration.setProperty(Environment.HBM2DDL_AUTO, props.getProperty("hibernate.hbm2ddl.auto", "update"));
                configuration.setProperty(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                configuration.setProperty(Environment.URL, props.getProperty("spring.datasource.url"));
                configuration.setProperty(Environment.USER, props.getProperty("spring.datasource.username"));
                configuration.setProperty(Environment.PASS, props.getProperty("spring.datasource.password"));

                configuration.addAnnotatedClass(Employe.class);
                configuration.addAnnotatedClass(Projet.class);
                configuration.addAnnotatedClass(Tache.class);
                configuration.addAnnotatedClass(EmployeTache.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                throw new RuntimeException("Erreur de création de SessionFactory", e);
            }
        }
        return sessionFactory;
    }
}
