package org.georchestra.pluievolution.core.entity.ref;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.pluievolution.core.common.LongId;

import javax.persistence.*;

/**
 * @author NCA20245
 */

@Entity
@Table(name = "status")
@ToString
@Getter  @Setter
public class PluiRequestStatusEntity implements LongId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "value", length = 50)
    private PluiRequestStatusEntityEnum value;

    public enum PluiRequestStatusEntityEnum {
        STATUT_NOUVEAU,
        STATUT_ANALYSE_EN_COURS,
        STATUT_PRODUCTION_EN_COURS,
        STATUT_EN_ATTENTE_VALIDATION_COMMUNE,
        STATUT_VALIDE_COMMUNE,
        STATUT_DEMANDE_NON_RECEVABLE,
        STATUT_DEMANDE_REFORMULEE
    }

    public static PluiRequestStatusEntityEnum fromValue(String text) {
        for (PluiRequestStatusEntityEnum b : PluiRequestStatusEntityEnum.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PluiRequestStatusEntity)) return false;

        PluiRequestStatusEntity that = (PluiRequestStatusEntity) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        return getValue() == that.getValue();
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getValue().hashCode();
        return result;
    }
}
