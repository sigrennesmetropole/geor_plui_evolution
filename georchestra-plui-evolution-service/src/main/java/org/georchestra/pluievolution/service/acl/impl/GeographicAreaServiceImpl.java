package org.georchestra.pluievolution.service.acl.impl;

import org.georchestra.pluievolution.core.dao.acl.GeographicAreaDao;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.entity.acl.GeographicAreaEntity;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.mapper.GeographicAreaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeographicAreaServiceImpl implements GeographicAreaService {

    @Autowired
    GeographicAreaDao geographicAreaDao;

    @Autowired
    GeographicAreaMapper geographicAreaMapper;

    @Override
    public GeographicAreaEntity getGeographicAreaByCodeInsee(String codeInsee) {
        return geographicAreaDao.findByCodeInsee(codeInsee);
    }

    @Override
    public GeographicAreaEntity getGeographicAreaByNom(String nom) {
        // Appliquer d'abord le formatage qu'il faut au nom avant de lancer la requete
        return geographicAreaDao.findByNom(nom);
    }

    @Override
    public List<GeographicArea> getAllGeographicArea() {
        return geographicAreaMapper.entitiesToDto(geographicAreaDao.findAll());
    }
}
