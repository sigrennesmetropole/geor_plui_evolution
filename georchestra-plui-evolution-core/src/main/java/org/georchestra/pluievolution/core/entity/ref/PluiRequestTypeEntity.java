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
@Table(name = "request_type")
@ToString
@Getter
@Setter
public class PluiRequestTypeEntity implements LongId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "value", length = 20)
    private PluiRequestTypeEntity.PluiRequestTypeEntityEnum value;

    public enum PluiRequestTypeEntityEnum {
        TYPE_COMMUNE,
        TYPE_INTERCOMMUNE,
        TYPE_METROPOLITAIN
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PluiRequestTypeEntity)) return false;

        PluiRequestTypeEntity that = (PluiRequestTypeEntity) o;

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
