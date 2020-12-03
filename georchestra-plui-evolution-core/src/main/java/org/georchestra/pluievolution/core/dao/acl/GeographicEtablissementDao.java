package org.georchestra.pluievolution.core.dao.acl;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.acl.GeographicEtablissementEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface GeographicEtablissementDao extends QueryDslDao<GeographicEtablissementEntity, Long> {
    /**
     * Permet de trouver un etablissement par son nom
     * @param nom
     * @return
     */
    GeographicEtablissementEntity findByNom(String nom);

    /**
     * Permet de trouver un etablisssement par son code insee
     * @param codeInsee
     * @return
     */
    GeographicEtablissementEntity findByCodeInsee(String codeInsee);
}
