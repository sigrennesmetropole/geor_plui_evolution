package org.georchestra.pluievolution.core.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * Interface pour les requêtes QueryDsl
 *
 * @author FNI18300
 *
 * @param <E>
 * @param <P>
 */
@NoRepositoryBean
public interface QueryDslDao<E, P extends Serializable> extends JpaRepository<E, P> {

}
