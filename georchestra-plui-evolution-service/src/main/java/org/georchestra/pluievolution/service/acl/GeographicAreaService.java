package org.georchestra.pluievolution.service.acl;

import com.vividsolutions.jts.geom.Geometry;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.georchestra.pluievolution.service.exception.ApiServiceException;

import java.util.List;

public interface GeographicAreaService {

    /**
     * Obtnir une geographic area a partir de son code insee
     * @param codeInsee
     * @return
     */
    GeographicArea getGeographicAreaByCodeInsee(String codeInsee);

    /**
     * obtenir une geographic area à partir de son nom
     * @param nom
     * @return
     */
    GeographicArea getGeographicAreaByNom(String nom);

    /**
     * Permet de recuperer la liste de toutes les geographic area
     * @return
     */
    List<GeographicArea> getAllGeographicArea();

    /**
     * Obtenir la zone accessible à l'utilisateur notamment sa commune
     * @return
     */
    GeographicArea getCurrentUserArea() throws ApiServiceException;

    /**
     * Obtenir la commune à laquelle appartient appartient un point donné
     */
    GeographicAreaEntity getGeographicAreaByPoint(Geometry point) throws ApiServiceException;

    /**
     * Permet d'obtneir une geographic area entity par son code insee
     * @param codeInsee
     * @return
     */
    public GeographicAreaEntity getGeographicAreaEntityByCodeInsee(String codeInsee);
}
