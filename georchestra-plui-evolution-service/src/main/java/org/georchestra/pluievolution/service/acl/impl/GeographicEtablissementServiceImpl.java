package org.georchestra.pluievolution.service.acl.impl;

import org.georchestra.pluievolution.core.dao.acl.GeographicEtablissementDao;
import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.core.entity.acl.GeographicEtablissementEntity;
import org.georchestra.pluievolution.service.acl.GeographicEtablissementService;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.helper.authentification.AuthentificationHelper;
import org.georchestra.pluievolution.service.mapper.GeographicEtablissementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeographicEtablissementServiceImpl implements GeographicEtablissementService {

    @Autowired
    GeographicEtablissementDao geographicEtablissementDao;

    @Autowired
    GeographicEtablissementMapper geographicEtablissementMapper;

    @Autowired
    AuthentificationHelper authentificationHelper;

    @Override
    public List<GeographicEtablissement> getAllEtablissement() {
        return geographicEtablissementMapper.entitiesToDto(geographicEtablissementDao.findAll());
    }

    @Override
    public GeographicEtablissement getCurrentUserEtablissement() throws ApiServiceException {
        String nom = authentificationHelper.getUsername();
        GeographicEtablissementEntity entity = geographicEtablissementDao.findByNom(nom);
        if (entity == null) {
            throw new ApiServiceException("Organisation inconnue", "404");
        }
        return geographicEtablissementMapper.entityToDto(entity);
    }
}
