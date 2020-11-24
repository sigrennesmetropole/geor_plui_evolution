package org.georchestra.pluievolution.core.dao.ref;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.ref.RequestTypeEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestTypeDao extends QueryDslDao<RequestTypeEntity, Long> {
}
