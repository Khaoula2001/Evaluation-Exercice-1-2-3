package ma.projet.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Properties props = new Properties();
            try (InputStream in = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (in != null) {
                    props.load(in);
                } else {
                    throw new RuntimeException("application.properties not found in classpath");
                }
            }
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
            builder.applySettings((Properties) props.clone());
            StandardServiceRegistry registry = builder.build();

            MetadataSources sources = new MetadataSources(registry)
                    .addAnnotatedClass(ma.projet.classes.Categorie.class)
                    .addAnnotatedClass(ma.projet.classes.Produit.class)
                    .addAnnotatedClass(ma.projet.classes.Commande.class)
                    .addAnnotatedClass(ma.projet.classes.LigneCommandeProduit.class);

            return sources.buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Initial SessionFactory creation failed: " + e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) sessionFactory.close();
    }
}
