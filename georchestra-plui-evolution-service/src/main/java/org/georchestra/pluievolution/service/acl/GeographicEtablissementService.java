package org.georchestra.pluievolution.service.acl;

import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.service.exception.ApiServiceException;

import java.util.List;

public interface GeographicEtablissementService {
    /**
     * Permet de recuperer la liste de toutes les geographiques area
     * @return
     */
    List<GeographicEtablissement> getAllEtablissement();

    /**
     * Permet d'obtenir l'etablissement de l'utilisatur connecté
     * @return
     */
    GeographicEtablissement getCurrentUserEtablissement() throws ApiServiceException;
}
