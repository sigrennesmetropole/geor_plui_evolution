package org.georchestra.pluievolution.service.acl.impl;

import org.georchestra.pluievolution.core.dao.acl.GeographicEtablissementDao;
import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.core.dto.User;
import org.georchestra.pluievolution.core.entity.acl.GeographicEtablissementEntity;
import org.georchestra.pluievolution.service.acl.GeographicEtablissementService;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.mapper.GeographicEtablissementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeographicEtablissementServiceImpl implements GeographicEtablissementService {

    @Autowired
    GeographicEtablissementDao geographicEtablissementDao;

    @Autowired
    GeographicEtablissementMapper geographicEtablissementMapper;

    @Override
    public List<GeographicEtablissement> getAllEtablissement() {
        return geographicEtablissementMapper.entitiesToDto(geographicEtablissementDao.findAll());
    }

    @Override
    public GeographicEtablissement getCurrentUserEtablissement() throws ApiServiceException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User details  = (User) authentication.getDetails();
        String nom = details.getOrganization();
        GeographicEtablissementEntity entity = geographicEtablissementDao.findByNom(nom);
        if (entity == null) {
            throw new ApiServiceException("Organisation inconnue", "404");
        }
        return geographicEtablissementMapper.entityToDto(entity);
    }
}
