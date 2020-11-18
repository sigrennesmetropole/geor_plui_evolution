package org.georchestra.pluievolution.core.dao.acl;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface GeographicAreaDao extends QueryDslDao<GeographicAreaEntity, Long> {

}
