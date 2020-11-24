package org.georchestra.pluievolution.core.dao.ref;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.ref.PluiRequestStatusEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PluiRequestStatusDao extends QueryDslDao<PluiRequestStatusEntity, Long> {
}
