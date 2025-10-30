package ma.projet.service;

import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.classes.Produit;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class ProduitService implements IDao<Produit, Long> {
    private Session getSession(){
        return HibernateUtil.getSessionFactory().openSession();
    }
    @Override
    public boolean create(Produit o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try {
            s.save(o);
            tx.commit();
            return true;
        } catch (Exception e){ tx.rollback(); throw e; }
        finally { s.close(); }
    }

    @Override
    public boolean update(Produit o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try { s.update(o); tx.commit(); return true; }
        catch (Exception e){ tx.rollback(); throw e; }
        finally { s.close(); }
    }

    @Override
    public boolean delete(Produit o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try { s.delete(o); tx.commit(); return true; }
        catch (Exception e){ tx.rollback(); throw e; }
        finally { s.close(); }
    }

    @Override
    public Produit getById(Long id) {
        Session s = getSession();
        try { return s.get(Produit.class, id); }
        finally { s.close(); }
    }

    @Override
    public List<Produit> getAll() {
        Session s = getSession();
        try { return s.createQuery("from Produit", Produit.class).list(); }
        finally { s.close(); }
    }

    // Liste des produits par catégorie
    public List<Produit> getByCategorie(Categorie cat){
        Session s = getSession();
        try {
            return s.createQuery("from Produit p where p.categorie = :c order by p.reference", Produit.class)
                    .setParameter("c", cat)
                    .list();
        } finally { s.close(); }
    }

    // Produits commandés entre deux dates (liste distincte)
    public List<Produit> getCommandesBetween(LocalDate d1, LocalDate d2){
        Session s = getSession();
        try {
            return s.createQuery(
                    "select distinct p from LigneCommandeProduit l join l.commande c join l.produit p " +
                    "where c.date between :d1 and :d2 order by p.reference", Produit.class)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .list();
        } finally { s.close(); }
    }

    // Produits d'une commande donnée avec quantité
    public List<Object[]> getProduitDetailsByCommande(Long commandeId){
        Session s = getSession();
        try {
            return s.createQuery(
                    "select p.reference, p.prix, l.quantite from LigneCommandeProduit l " +
                    "join l.produit p join l.commande c where c.id = :id order by p.reference", Object[].class)
                    .setParameter("id", commandeId)
                    .list();
        } finally { s.close(); }
    }

    // Produits dont le prix > minPrix (nommée)
    public List<Produit> getPrixSuperieur(float minPrix){
        Session s = getSession();
        try {
            return s.createNamedQuery("Produit.findByPrixSup", Produit.class)
                    .setParameter("minPrix", minPrix)
                    .getResultList();
        } finally { s.close(); }
    }
}
