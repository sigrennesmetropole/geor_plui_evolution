package org.georchestra.pluievolution.core.dao.acl;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.acl.GeographicEtablissementEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface GeographicEtablissementDao extends QueryDslDao<GeographicEtablissementEntity, Long> {

}
