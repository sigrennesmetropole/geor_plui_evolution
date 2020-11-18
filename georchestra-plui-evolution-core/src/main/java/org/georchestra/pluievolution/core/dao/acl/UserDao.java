package org.georchestra.pluievolution.core.dao.acl;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.acl.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends QueryDslDao<UserEntity, Long> {

	UserEntity findByLogin(String login);

}
