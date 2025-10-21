package ma.projet.classes;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ligne_commande_produit")
public class LigneCommandeProduit {
    @EmbeddedId
    private PK id = new PK();

    @ManyToOne
    @MapsId("commandeId")
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne
    @MapsId("produitId")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private int quantite;

    public LigneCommandeProduit() {}

    public LigneCommandeProduit(Commande commande, Produit produit, int quantite) {
        this.commande = commande;
        this.produit = produit;
        this.quantite = quantite;
        this.id = new PK(commande.getId(), produit.getId());
    }

    public PK getId() { return id; }
    public void setId(PK id) { this.id = id; }

    public Commande getCommande() { return commande; }
    public void setCommande(Commande commande) { this.commande = commande; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    @Embeddable
    public static class PK implements Serializable {
        private Long commandeId;
        private Long produitId;

        public PK() {}
        public PK(Long commandeId, Long produitId) {
            this.commandeId = commandeId;
            this.produitId = produitId;
        }
        public Long getCommandeId() { return commandeId; }
        public void setCommandeId(Long commandeId) { this.commandeId = commandeId; }
        public Long getProduitId() { return produitId; }
        public void setProduitId(Long produitId) { this.produitId = produitId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PK pk = (PK) o;
            return Objects.equals(commandeId, pk.commandeId) && Objects.equals(produitId, pk.produitId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(commandeId, produitId);
        }
    }
}
