package ma.projet.classes;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EmployeTacheId implements Serializable {
    private Long employeId;
    private Long tacheId;

    public EmployeTacheId() {}

    public EmployeTacheId(Long employeId, Long tacheId) {
        this.employeId = employeId;
        this.tacheId = tacheId;
    }

    public Long getEmployeId() { return employeId; }
    public void setEmployeId(Long employeId) { this.employeId = employeId; }
    public Long getTacheId() { return tacheId; }
    public void setTacheId(Long tacheId) { this.tacheId = tacheId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeTacheId that = (EmployeTacheId) o;
        return Objects.equals(employeId, that.employeId) && Objects.equals(tacheId, that.tacheId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeId, tacheId);
    }
}
