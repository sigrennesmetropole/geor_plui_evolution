package org.georchestra.pluievolution.service.acl.impl;

import com.vividsolutions.jts.geom.Geometry;
import org.georchestra.pluievolution.core.dao.acl.GeographicAreaDao;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.dto.User;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.mapper.GeographicAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GeographicAreaServiceImpl implements GeographicAreaService {

    @Autowired
    GeographicAreaDao geographicAreaDao;

    @Autowired
    GeographicAreaMapper geographicAreaMapper;

    @Override
    public GeographicArea getGeographicAreaByCodeInsee(String codeInsee) {
        return geographicAreaMapper.entityToDto(geographicAreaDao.findByCodeInsee(codeInsee));
    }

    @Override
    public GeographicArea getGeographicAreaByNom(String nom) {
        // Appliquer d'abord le formatage qu'il faut au nom avant de lancer la requete
        return geographicAreaMapper.entityToDto(geographicAreaDao.findByNom(nom));
    }

    @Override
    public List<GeographicArea> getAllGeographicArea() {
        return geographicAreaMapper.entitiesToDto(geographicAreaDao.findAll());
    }

    @Override
    public Geometry getCurrentUserArea() throws ApiServiceException {
        // On recupere l'organisation a laquelle appartient le user connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User details  = (User) authentication.getDetails();
        String nom = details.getOrganization();
        GeographicAreaEntity entity = geographicAreaDao.findByNom(nom);
        if (entity == null) {
            throw new ApiServiceException("Organisation inconnue", "404");
        }
        return entity.getGeometry();
    }
}
