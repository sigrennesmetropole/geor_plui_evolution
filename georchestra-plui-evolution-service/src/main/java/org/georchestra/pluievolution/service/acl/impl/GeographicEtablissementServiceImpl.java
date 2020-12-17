package org.georchestra.pluievolution.service.acl.impl;

import org.georchestra.pluievolution.core.dao.acl.GeographicEtablissementDao;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.core.entity.acl.GeographicEtablissementEntity;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
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
    GeographicAreaService geographicAreaService;

    @Autowired
    AuthentificationHelper authentificationHelper;

    @Override
    public GeographicEtablissement getGeographicEtablissementByCodeInsee(String codeInsee) {
        return geographicEtablissementMapper.entityToDto(geographicEtablissementDao.findByCodeInsee(codeInsee));
    }

    @Override
    public List<GeographicEtablissement> getAllEtablissement() {
        return geographicEtablissementMapper.entitiesToDto(geographicEtablissementDao.findAll());
    }

    @Override
    public GeographicEtablissement getCurrentUserEtablissement() throws ApiServiceException {
        // nom organisation == nom geographic area
        // on recupere donc la geographic area a partir du nom
        String nom = authentificationHelper.getOrganisation();
        GeographicArea geographicArea = geographicAreaService.getGeographicAreaByNom(nom);
        GeographicEtablissementEntity entity = geographicEtablissementDao.findByCodeInsee(geographicArea.getCodeInsee());

        if (entity == null) {
            throw new ApiServiceException("Organisation inconnue", "404");
        }
        return geographicEtablissementMapper.entityToDto(entity);
    }
}
