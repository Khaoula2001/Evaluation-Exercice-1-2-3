package ma.projet.service;

import ma.projet.classes.*;
import ma.projet.dao.IDao;
import ma.projet.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProduitService implements IDao<Produit> {
    @Override
    public Produit save(Produit p) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(p);
            tx.commit();
            return p;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Produit update(Produit p) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            p = (Produit) session.merge(p);
            tx.commit();
            return p;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(Produit p) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(session.contains(p) ? p : session.merge(p));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Produit findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Produit.class, id);
        }
    }

    @Override
    public List<Produit> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Produit", Produit.class).getResultList();
        }
    }

    // Custom methods
    public List<Produit> listByCategorie(Categorie categorie) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Produit p where p.categorie = :c", Produit.class)
                    .setParameter("c", categorie)
                    .getResultList();
        }
    }

    public List<Object[]> findByCommande(Commande commande) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select p.reference, p.prix, l.quantite from LigneCommandeProduit l join l.produit p where l.commande = :cmd";
            return session.createQuery(hql, Object[].class)
                    .setParameter("cmd", commande)
                    .getResultList();
        }
    }

    public List<Produit> findOrderedBetweenDates(LocalDate d1, LocalDate d2) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select distinct p from LigneCommandeProduit l join l.produit p join l.commande c where c.date between :d1 and :d2";
            return session.createQuery(hql, Produit.class)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .getResultList();
        }
    }

    public List<Produit> findPrixSup100UsingNamedQuery() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Produit.findPrixSup100", Produit.class).getResultList();
        }
    }
}
