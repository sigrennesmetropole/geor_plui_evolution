package org.georchestra.pluievolution.core.entity.request;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

import org.georchestra.pluievolution.core.common.LongId;

import com.vividsolutions.jts.geom.Geometry;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;import org.georchestra.pluievolution.core.entity.ref.PluiRequestTypeEntity;
import org.georchestra.pluievolution.core.entity.ref.PluiRequestStatusEntity;

/**
 * @author FNI18300
 *
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "plui_request")
public class PluiRequestEntity implements LongId {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "uuid", nullable = false, columnDefinition = "uuid")
	private UUID uuid;

	@Column(name = "redmine_id")
	private String redmineId;

	@Column(name = "subject", length = 30, nullable = false)
	private String subject;

	@Column(name = "object", length = 300, nullable = false)
	private String object;

	@Column(name = "comment", length = 1024)
	private String comment;

	@Column(name = "initiator", length = 150)
	private String initiator;

	@Column(name = "creation_date", nullable = false)
	private Date creationDate;

	@Column(name = "geometry", columnDefinition = "Geometry")
	private Geometry geometry;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "status_id", referencedColumnName = "id")
	private PluiRequestStatusEntity status;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_id", referencedColumnName = "id")
	private PluiRequestTypeEntity type;

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
		if (!(obj instanceof PluiRequestEntity)) {
			return false;
		}
		PluiRequestEntity other = (PluiRequestEntity) obj;
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
