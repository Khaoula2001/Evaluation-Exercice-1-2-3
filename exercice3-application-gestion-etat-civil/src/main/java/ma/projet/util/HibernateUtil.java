package ma.projet.util;

import ma.projet.beans.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Properties props = new Properties();
            try (InputStream in = HibernateUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (in != null) props.load(in);
            } catch (IOException e) {
                throw new RuntimeException("Cannot load application.properties", e);
            }

            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
            registryBuilder.applySettings((Properties) props.clone());
            StandardServiceRegistry registry = registryBuilder.build();

            MetadataSources sources = new MetadataSources(registry)
                    .addAnnotatedClass(Personne.class)
                    .addAnnotatedClass(Homme.class)
                    .addAnnotatedClass(Femme.class)
                    .addAnnotatedClass(Mariage.class);

            return sources.buildMetadata().buildSessionFactory();
        } catch (Exception ex) {
            throw new RuntimeException("SessionFactory build failed", ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
