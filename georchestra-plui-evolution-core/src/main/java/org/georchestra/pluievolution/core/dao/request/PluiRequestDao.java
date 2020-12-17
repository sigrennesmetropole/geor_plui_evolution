package org.georchestra.pluievolution.core.dao.request;

import org.georchestra.pluievolution.core.dao.QueryDslDao;
import org.georchestra.pluievolution.core.entity.request.PluiRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@SuppressWarnings("squid:S100")
@Repository
public interface PluiRequestDao extends QueryDslDao<PluiRequestEntity, Long> {

    Page<PluiRequestEntity> findAll(Pageable pageable);

    /**
     * Permet de trouver un PluiRequestEntity à partir de son uuid
     * @param uuid
     * @return
     */
    PluiRequestEntity findByUuid(UUID uuid);

    /**
     * Permet de supprimer une piece jointe a partir de son uuid
     * @param uuid
     */
    void deleteByUuid(UUID uuid);

}
