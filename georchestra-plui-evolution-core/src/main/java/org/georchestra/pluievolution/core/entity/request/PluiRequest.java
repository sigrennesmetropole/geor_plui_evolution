/**
 * 
 */
package org.georchestra.pluievolution.core.entity.request;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.georchestra.pluievolution.core.common.LongId;

import com.vividsolutions.jts.geom.Geometry;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author FNI18300
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "plui_request")
public class PluiRequest implements LongId {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "uuid", nullable = false)
	private UUID uuid;

	@Column(name = "redmine_id")
	private String redmineId;

	@Column(name = "description", length = 1024, nullable = false)
	private String description;

	@Column(name = "comment", length = 1024)
	private String comment;

	@Column(name = "initiator", length = 150)
	private String initiator;

	@Column(name = "creation_date", nullable = false)
	private Date creationDate;

	@Column(name = "geometry", columnDefinition = "Geometry")
	private Geometry geometry;

	// TODO Status

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCreationDate() == null) ? 0 : getCreationDate().hashCode());
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PluiRequest)) {
			return false;
		}
		PluiRequest other = (PluiRequest) obj;
		if (getCreationDate() == null) {
			if (other.getCreationDate() != null) {
				return false;
			}
		} else if (!getCreationDate().equals(other.getCreationDate())) {
			return false;
		}
		if (getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!getId().equals(other.getId())) {
			return false;
		}
		return true;
	}

}
