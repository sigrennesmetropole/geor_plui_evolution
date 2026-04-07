package org.georchestra.pluievolution.core.entity.request;

import java.util.Date;
import java.util.UUID;

import org.georchestra.pluievolution.core.common.LongId;
import org.georchestra.pluievolution.core.dto.PluiRequestStatus;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.locationtech.jts.geom.Geometry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
public class PluiRequestEntity implements LongId {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "uuid", nullable = false, columnDefinition = "uuid")
	private UUID uuid;

	@Column(name = "redmine_id")
	private Integer redmineId;

	@Column(name = "subject", length = 130, nullable = false)
	private String subject;

	@Column(name = "object", length = 300, nullable = false)
	private String object;

	@Column(name = "plui_procedure", length = 50, nullable = true)
	private String pluiProcedure;

	@Column(name = "concertation")
	private String concertation;

	@Column(name = "approbation")
	private String approbation;

	@Column(name = "comment", length = 1024)
	private String comment;

	@Column(name = "initiator", length = 150)
	private String initiator;

	@Column(name = "creation_date", nullable = false)
	private Date creationDate;

	@Column(name = "geometry", columnDefinition = "geometry")
	private Geometry geometry;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 50)
	private PluiRequestStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", length = 20)
	private PluiRequestType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_id")
	private GeographicAreaEntity area;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof PluiRequestEntity that)) {
			return false;
		}
		if( getId() != null && getId().equals(that.getId())) {
			return true;
		}
		
        return getUuid().equals(that.getUuid());
    }

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + getUuid().hashCode();
		return result;
	}
}
