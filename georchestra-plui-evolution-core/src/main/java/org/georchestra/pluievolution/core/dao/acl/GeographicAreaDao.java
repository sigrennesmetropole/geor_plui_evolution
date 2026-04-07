package org.georchestra.pluievolution.core.dao.acl;


import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.locationtech.jts.geom.Geometry;
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
            value = "SELECT ga FROM GeographicAreaEntity ga WHERE ga.codeInsee <> :codeInseeRM AND intersects(ga.geometry, :point) = true"
    )
    GeographicAreaEntity getByCoords(@Param("point") Geometry point, @Param("codeInseeRM") String codeInseeRM);

    @Query(
            value = "SELECT COUNT(ga) FROM GeographicAreaEntity ga WHERE ga.id = :areaId AND contains(ga.geometry, :point) = true"
    )
    long isPointInGeographicArea(@Param("areaId") Long areaId, @Param("point") Geometry point);

}

