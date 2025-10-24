package ma.projet.beans;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mariages")
public class Mariage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "homme_id")
    private Homme homme;

    @ManyToOne(optional = false)
    @JoinColumn(name = "femme_id")
    private Femme femme;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin; // nullable means en cours

    @Column(name = "nbr_enfant")
    private int nbrEnfant;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Homme getHomme() { return homme; }
    public void setHomme(Homme homme) { this.homme = homme; }

    public Femme getFemme() { return femme; }
    public void setFemme(Femme femme) { this.femme = femme; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public int getNbrEnfant() { return nbrEnfant; }
    public void setNbrEnfant(int nbrEnfant) { this.nbrEnfant = nbrEnfant; }

    @Transient
    public boolean isEnCours() { return dateFin == null; }
}
