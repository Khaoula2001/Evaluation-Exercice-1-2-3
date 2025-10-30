package ma.projet.classes;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commande")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_cmd", nullable = false)
    private LocalDate date; // date de la commande

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneCommandeProduit> lignes = new ArrayList<>();

    public Commande() {}

    public Commande(LocalDate date) {
        this.date = date;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<LigneCommandeProduit> getLignes() { return lignes; }
    public void setLignes(List<LigneCommandeProduit> lignes) { this.lignes = lignes; }

    public void addLigne(Produit produit, int quantite){
        LigneCommandeProduit l = new LigneCommandeProduit(this, produit, quantite);
        lignes.add(l);
        produitLineBackref(l);
    }

    private void produitLineBackref(LigneCommandeProduit l){
        // helper to ensure both sides set
        l.setCommande(this);
    }
}
