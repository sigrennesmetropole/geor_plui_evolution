package org.georchestra.pluievolution.core.dao.acl;

import com.vividsolutions.jts.geom.Geometry;
import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GeographicAreaDao extends QueryDslDao<GeographicAreaEntity, Long> {
    /**
     * Permet d'obtenir une geographique area a partir de son coe Insee
     * @param codeInsee
     * @return
     */
    GeographicAreaEntity findByCodeInsee(String codeInsee);

    /**
     * Trouver une geeographic area a partir de son nom
     * @param nom
     * @return
     */
    GeographicAreaEntity findByNom(String nom);

    @Query(
            value = "SELECT * FROM geographic_area ga WHERE ga.codeinsee <> '243500139' AND ST_Intersects(ga.geometry, :point)",
            nativeQuery = true
    )
    GeographicAreaEntity getByCoords(@Param("point") Geometry point);




}
