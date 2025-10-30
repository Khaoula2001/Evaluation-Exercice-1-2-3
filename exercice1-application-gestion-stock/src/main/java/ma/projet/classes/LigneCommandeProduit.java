package ma.projet.classes;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ligne_commande_produit")
public class LigneCommandeProduit {

    @Embeddable
    public static class PK implements Serializable {
        @Column(name = "commande_id")
        private Long commandeId;
        @Column(name = "produit_id")
        private Long produitId;

        public PK() {}
        public PK(Long commandeId, Long produitId) {
            this.commandeId = commandeId;
            this.produitId = produitId;
        }
        public Long getCommandeId() { return commandeId; }
        public Long getProduitId() { return produitId; }
        public void setCommandeId(Long commandeId) { this.commandeId = commandeId; }
        public void setProduitId(Long produitId) { this.produitId = produitId; }

        @Override
        public boolean equals(Object o){
            if(this==o) return true;
            if(o==null || getClass()!=o.getClass()) return false;
            PK pk=(PK)o;
            return java.util.Objects.equals(commandeId, pk.commandeId) &&
                   java.util.Objects.equals(produitId, pk.produitId);
        }
        @Override
        public int hashCode(){
            return java.util.Objects.hash(commandeId, produitId);
        }
    }

    @EmbeddedId
    private PK id = new PK();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("commandeId")
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produitId")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(nullable = false)
    private int quantite;

    public LigneCommandeProduit() {}

    public LigneCommandeProduit(Commande commande, Produit produit, int quantite) {
        this.commande = commande;
        this.produit = produit;
        this.quantite = quantite;
        if (commande != null) this.id.setCommandeId(commande.getId());
        if (produit != null) this.id.setProduitId(produit.getId());
    }

    public PK getId() { return id; }
    public void setId(PK id) { this.id = id; }

    public Commande getCommande() { return commande; }
    public void setCommande(Commande commande) { this.commande = commande; if(commande!=null) this.id.setCommandeId(commande.getId()); }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; if(produit!=null) this.id.setProduitId(produit.getId()); }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
}
