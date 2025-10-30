package ma.projet.service;

import ma.projet.classes.Commande;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ma.projet.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class CommandeService implements IDao<Commande, Long> {
    private Session getSession(){
        return HibernateUtil.getSessionFactory().openSession();
    }
    @Override
    public boolean create(Commande o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try {
            s.save(o);
            tx.commit();
            return true;
        } catch (Exception e){
            tx.rollback();
            throw e;
        } finally { s.close(); }
    }

    @Override
    public boolean update(Commande o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try {
            s.update(o);
            tx.commit();
            return true;
        } catch (Exception e){
            tx.rollback();
            throw e;
        } finally { s.close(); }
    }

    @Override
    public boolean delete(Commande o) {
        Session s = getSession();
        Transaction tx = s.beginTransaction();
        try {
            s.delete(o);
            tx.commit();
            return true;
        } catch (Exception e){
            tx.rollback();
            throw e;
        } finally { s.close(); }
    }

    @Override
    public Commande getById(Long id) {
        Session s = getSession();
        try { return s.get(Commande.class, id); } finally { s.close(); }
    }

    @Override
    public List<Commande> getAll() {
        Session s = getSession();
        try { return s.createQuery("from Commande", Commande.class).list(); }
        finally { s.close(); }
    }

    public List<Commande> getBetween(LocalDate d1, LocalDate d2){
        Session s = getSession();
        try {
            return s.createQuery("from Commande c where c.date between :d1 and :d2 order by c.date", Commande.class)
                    .setParameter("d1", d1)
                    .setParameter("d2", d2)
                    .list();
        } finally { s.close(); }
    }
}
