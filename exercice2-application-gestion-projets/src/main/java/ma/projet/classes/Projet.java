package ma.projet.classes;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "projets")
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    // Chef de projet
    @ManyToOne
    @JoinColumn(name = "chef_id")
    private Employe chef;

    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tache> taches = new HashSet<>();

    public Projet() {}

    public Projet(String nom, Date dateDebut, Date dateFin, Employe chef) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.chef = chef;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public Employe getChef() { return chef; }
    public void setChef(Employe chef) { this.chef = chef; }

    public Set<Tache> getTaches() { return taches; }
    public void setTaches(Set<Tache> taches) { this.taches = taches; }
}
