package org.georchestra.pluievolution.core.dao.config;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.configuration.ConfigurationEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationDao extends QueryDslDao<ConfigurationEntity, String> {
    /**
     * Récupère la configuration associée à un code
     * @param code Le code de la configuration à récupérer
     * @return La configuration associée au code
     */
    ConfigurationEntity findByCode(String code);
}
