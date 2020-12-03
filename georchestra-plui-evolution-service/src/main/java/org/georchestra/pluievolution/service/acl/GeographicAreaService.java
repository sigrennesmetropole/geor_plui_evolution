package org.georchestra.pluievolution.service.acl;

import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;

import java.util.List;

public interface GeographicAreaService {

    /**
     * Obtnir une geographic area a partir de son code insee
     * @param codeInsee
     * @return
     */
    GeographicAreaEntity getGeographicAreaByCodeInsee(String codeInsee);

    /**
     * obtenir une geographic area à partir de son nom
     * @param nom
     * @return
     */
    GeographicAreaEntity getGeographicAreaByNom(String nom);

    /**
     * Permet de recuperer la liste de toutes les geographic area
     * @return
     */
    List<GeographicArea> getAllGeographicArea();
}
