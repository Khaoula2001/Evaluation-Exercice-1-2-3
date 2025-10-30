package ma.projet.beans;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "femmes")
@NamedQueries({
        @NamedQuery(name = "Femme.marriedAtLeastTwice",
                query = "SELECT f FROM Femme f WHERE size(f.mariages) >= 2")
})
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "Femme.countChildrenBetween",
                query = "SELECT COALESCE(SUM(m.nbr_enfant),0) AS cnt FROM mariages m " +
                        "WHERE m.femme_id = :femmeId AND m.date_debut BETWEEN :startDate AND :endDate",
                resultSetMapping = "CountMapping"
        )
})
@SqlResultSetMapping(
        name = "CountMapping",
        columns = {
                @ColumnResult(name = "cnt")
        }
)
public class Femme extends Personne {

    @OneToMany(mappedBy = "femme", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mariage> mariages = new ArrayList<>();

    public List<Mariage> getMariages() { return mariages; }
    public void setMariages(List<Mariage> mariages) { this.mariages = mariages; }
}
