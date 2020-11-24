package org.georchestra.pluievolution.core.dao.ref;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.ref.StatusEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusDao extends QueryDslDao<StatusEntity, Long> {
}
