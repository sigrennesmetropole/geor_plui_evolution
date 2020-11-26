package org.georchestra.pluievolution.core.dao.ref;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.ref.PluiRequestTypeEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PluiRequestTypeDao extends QueryDslDao<PluiRequestTypeEntity, Long> {
    /**
     * Permet de recuperer un status avec son id a partir de sa value
     * @param type
     * @return
     */
    PluiRequestTypeEntity findByValue(PluiRequestTypeEntity.PluiRequestTypeEntityEnum type);
}
