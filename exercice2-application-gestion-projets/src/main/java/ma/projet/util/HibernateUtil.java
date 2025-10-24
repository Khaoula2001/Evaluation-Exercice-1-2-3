package ma.projet.util;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utilitaire minimal pour satisfaire l'exigence "HibernateUtil".
 * Avec Spring Boot nous n'en avons pas besoin, mais cette classe
 * peut créer une EMF JPA standard si nécessaire.
 */
public final class HibernateUtil {
    private static EntityManagerFactory emf;

    private HibernateUtil() {}

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            // Le nom "default" est utilisé par Spring Boot pour l'auto-config si un persistence.xml existe.
            // Ici on crée une EMF programmatique uniquement si nécessaire.
            emf = Persistence.createEntityManagerFactory("default");
        }
        return emf;
    }

    public static synchronized void close() {
        if (emf != null) {
            emf.close();
            emf = null;
        }
    }
}
