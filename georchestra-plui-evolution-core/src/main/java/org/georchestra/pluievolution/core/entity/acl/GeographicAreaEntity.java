/**
 * 
 */
package org.georchestra.pluievolution.core.entity.acl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.geolatte.geom.Geometry;
import org.georchestra.pluievolution.core.common.LongId;

import lombok.Data;

/**
 * Représente un ensemble de point pour désigner les communes et le centre de RM
 * etc... auquels peuvent être positionner les demandes PLUi
 * 
 * @author FNI18300
 *
 */
@Data
@Entity
@Table(name = "geographic_area")
public class GeographicAreaEntity implements LongId {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "nom", length = 255)
	private String nom;

	@Column(name = "codeinsee", length = 5)
	private String codeInsee;

	@Column(name = "geometry", columnDefinition = "Geometry")
	private Geometry geometry;
}
