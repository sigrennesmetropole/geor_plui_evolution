package org.georchestra.pluievolution.core.entity.acl;

import com.vividsolutions.jts.geom.Geometry;
import lombok.Getter;
import lombok.Setter;
import org.georchestra.pluievolution.core.common.LongId;

import javax.persistence.*;

/**
 * @author NCA20245
 */
@Getter
@Setter
@Entity
@Table(name = "geographic_area")
public class GeographicAreaEntity implements LongId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nom", length = 255, unique = true)
    private String nom;

    @Column(name = "codeinsee", length = 10, unique = true)
    private String codeInsee;

    // Dans cette geometrie seront enregistrés des polygones
    @Column(name = "geometry", columnDefinition = "Geometry")
    private Geometry geometry;

    @Column(name = "identifiant_redmine", length = 63)
    private String identifiantRedmine;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeographicEtablissementEntity)) return false;

        GeographicEtablissementEntity that = (GeographicEtablissementEntity) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        return getCodeInsee().equals(that.getCodeInsee());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getCodeInsee().hashCode();
        return result;
    }
}
