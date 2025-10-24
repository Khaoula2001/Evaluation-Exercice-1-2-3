package ma.projet.classes;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class EmployeTache {
    @EmbeddedId
    private EmployeTacheId id = new EmployeTacheId();

    @ManyToOne
    @MapsId("employeId")
    private Employe employe;

    @ManyToOne
    @MapsId("tacheId")
    private Tache tache;

    private LocalDate dateDebutReelle;
    private LocalDate dateFinReelle;

    public EmployeTache() {}

    public EmployeTache(Employe employe, Tache tache, LocalDate dateDebutReelle, LocalDate dateFinReelle) {
        this.employe = employe;
        this.tache = tache;
        this.dateDebutReelle = dateDebutReelle;
        this.dateFinReelle = dateFinReelle;
    }

    public EmployeTacheId getId() { return id; }
    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }
    public Tache getTache() { return tache; }
    public void setTache(Tache tache) { this.tache = tache; }
    public LocalDate getDateDebutReelle() { return dateDebutReelle; }
    public void setDateDebutReelle(LocalDate dateDebutReelle) { this.dateDebutReelle = dateDebutReelle; }
    public LocalDate getDateFinReelle() { return dateFinReelle; }
    public void setDateFinReelle(LocalDate dateFinReelle) { this.dateFinReelle = dateFinReelle; }
}
