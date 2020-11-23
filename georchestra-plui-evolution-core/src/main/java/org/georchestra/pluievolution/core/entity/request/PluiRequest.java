package org.georchestra.pluievolution.core.entity.request;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.georchestra.pluievolution.core.common.LongId;

import com.vividsolutions.jts.geom.Geometry;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.georchestra.pluievolution.core.entity.ged.AttachmentEntity;
import org.georchestra.pluievolution.core.entity.ref.RequestTypeEntity;
import org.georchestra.pluievolution.core.entity.ref.StatusEntity;

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

	@Column(name = "uuid", nullable = false)
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
	@JoinColumn(name = "area_id")
	private GeographicAreaEntity area;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "status_id")
	private StatusEntity status;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "attachment_id")
	private List<AttachmentEntity> attachments;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_id")
	private RequestTypeEntity type;

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
