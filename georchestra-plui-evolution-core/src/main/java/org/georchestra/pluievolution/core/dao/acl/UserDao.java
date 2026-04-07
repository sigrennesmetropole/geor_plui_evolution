package org.georchestra.pluievolution.core.dao.acl;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.acl.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends QueryDslDao<UserEntity, Long> {

	UserEntity findByLogin(String login);

}
