package org.georchestra.pluievolution.core.dao.acl;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.acl.EtablissementEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface EtablissementDao extends QueryDslDao<EtablissementEntity, Long> {

}
