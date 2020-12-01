/**
 * 
 */
package org.georchestra.pluievolution.core.entity.acl;

import javax.persistence.*;

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
@Table(name = "etablissement")
public class EtablissementEntity implements LongId {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "nom", length = 255)
	private String nom;

	@Column(name = "codeinsee", length = 10, unique = true)
	private String codeInsee;

	@Column(name = "geometry", columnDefinition = "Geometry")
	private Geometry geometry;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EtablissementEntity)) return false;

		EtablissementEntity that = (EtablissementEntity) o;

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
