package org.georchestra.pluievolution.service.acl;

import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;

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
}
