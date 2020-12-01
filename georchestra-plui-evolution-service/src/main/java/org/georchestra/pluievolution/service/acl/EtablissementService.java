package org.georchestra.pluievolution.service.acl;

import org.georchestra.pluievolution.core.dto.Etablissement;

import java.util.List;

public interface EtablissementService {
    /**
     * Permet de recuperer la liste de toutes les geographiques area
     * @return
     */
    List<Etablissement> getAllEtablissement();
}
