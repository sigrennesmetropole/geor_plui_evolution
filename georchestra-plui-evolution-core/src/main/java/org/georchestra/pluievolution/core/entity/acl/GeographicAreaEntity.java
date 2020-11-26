/**
 * 
 */
package org.georchestra.pluievolution.core.entity.acl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.Geometry;
import lombok.Getter;
import lombok.Setter;
import org.georchestra.pluievolution.core.common.LongId;

/**
 * Représente un ensemble de point pour désigner les communes et le centre de RM
 * etc... auquels peuvent être positionner les demandes PLUi
 * 
 * @author FNI18300
 *
 */
@Getter @Setter
@Entity
@Table(name = "geographic_area")
public class GeographicAreaEntity implements LongId {

	@Id
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "nom", length = 255)
	private String nom;

	@Column(name = "geometry", columnDefinition = "Geometry")
	private Geometry geometry;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GeographicAreaEntity)) return false;

		GeographicAreaEntity that = (GeographicAreaEntity) o;

		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getNom() != null ? !getNom().equals(that.getNom()) : that.getNom() != null) return false;
		return getGeometry() != null ? getGeometry().equals(that.getGeometry()) : that.getGeometry() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getNom() != null ? getNom().hashCode() : 0);
		result = 31 * result + (getGeometry() != null ? getGeometry().hashCode() : 0);
		return result;
	}
}
