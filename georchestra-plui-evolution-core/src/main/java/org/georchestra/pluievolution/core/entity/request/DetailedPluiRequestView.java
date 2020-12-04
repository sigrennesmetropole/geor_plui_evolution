package org.georchestra.pluievolution.core.entity.request;

import com.vividsolutions.jts.geom.Geometry;
import lombok.Getter;
import lombok.Setter;
import org.georchestra.pluievolution.core.dto.PluiRequestStatus;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Immutable
@Subselect("SELECT pr.id AS id, pr.comment AS comment, pr.creation_date AS creationDate, " +
        "pr.geometry AS geometry, pr.initiator AS initiator, " +
        "pr.object AS object, pr.status AS status, pr.subject AS subject, " +
        "pr.type AS type, pr.uuid AS uuid, " +
        "ga.code_insee AS codeInsee, ga.nom as nom " +
        "FROM plui_request pr LEFT JOIN geographic_area ga ON pr.area_id = ga.id")
@Synchronize({"PluiRequestEntity", "GeographicAreaEntity"})
@Getter @Setter
public class DetailedPluiRequestView {
    @Id
    private Long id;

    private UUID uuid;

    private String subject;

    private String object;

    private String comment;

    private String initiator;

    private Date creationDate;

    private Geometry geometry;

    private PluiRequestStatus status;

    private PluiRequestType type;

    private String nom;

    private String codeInsee;
}
