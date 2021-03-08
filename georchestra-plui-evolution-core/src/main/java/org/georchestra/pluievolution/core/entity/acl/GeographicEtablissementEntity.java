/**
 * 
 */
package org.georchestra.pluievolution.core.entity.acl;

import lombok.Getter;
import lombok.Setter;
import org.georchestra.pluievolution.core.common.LongId;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Représente un ensemble de point pour désigner les communes et le centre de RM
 * etc... auquels peuvent être positionner les demandes PLUi
 * 
 * @author FNI18300
 *
 */
@Getter @Setter
@Entity
@Table(name = "geographic_etablissement")
public class GeographicEtablissementEntity implements LongId {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "nom", length = 255)
	private String nom;

	@Column(name = "codeinsee", length = 10, unique = true)
	private String codeInsee;

	// Dans cette geometrie seront enregistrés des points
	@Column(name = "geometry", columnDefinition = "Geometry")
	private Geometry geometry;

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
