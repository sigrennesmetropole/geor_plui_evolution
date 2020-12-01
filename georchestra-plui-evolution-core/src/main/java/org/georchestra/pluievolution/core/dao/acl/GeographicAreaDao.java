package org.georchestra.pluievolution.core.dao.acl;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.acl.EtablissementEntity;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface GeographicAreaDao extends QueryDslDao<EtablissementEntity, Long> {
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
    GeographicAreaEntity findByName(String nom);
}
