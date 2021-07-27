package org.georchestra.pluievolution.service.acl;

import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.service.exception.ApiServiceException;

import java.util.List;

public interface GeographicEtablissementService {

    /**
     * Obtenir une geographic etablissement a partir de son code insee
     * @param codeInsee
     * @return
     */
    GeographicEtablissement getGeographicEtablissementByCodeInsee(String codeInsee);

    /**
     * Permet de recuperer la liste de toutes les geographiques area
     * @return
     */
    List<GeographicEtablissement> searchEtablissements();

    /**
     * Permet d'obtenir l'etablissement de l'utilisatur connecté
     * @return
     */
    GeographicEtablissement getCurrentUserEtablissement() throws ApiServiceException;
}
