package org.georchestra.pluievolution.core.dao.ref;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.ref.PluiRequestStatusEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PluiRequestStatusDao extends QueryDslDao<PluiRequestStatusEntity, Long> {
    /**
     * Permet de recuperer un status avec son id a partir de sa value
     * @param status
     * @return
     */
    PluiRequestStatusEntity findByValue(PluiRequestStatusEntity.PluiRequestStatusEntityEnum status);
}
