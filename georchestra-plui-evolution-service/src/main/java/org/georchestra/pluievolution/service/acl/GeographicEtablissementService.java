package org.georchestra.pluievolution.service.acl;

import org.georchestra.pluievolution.core.dto.GeographicEtablissement;

import java.util.List;

public interface GeographicEtablissementService {
    /**
     * Permet de recuperer la liste de toutes les geographiques area
     * @return
     */
    List<GeographicEtablissement> getAllEtablissement();
}
