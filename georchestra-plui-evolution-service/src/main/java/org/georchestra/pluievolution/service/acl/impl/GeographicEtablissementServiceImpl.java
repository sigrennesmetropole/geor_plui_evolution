package org.georchestra.pluievolution.service.acl.impl;

import org.georchestra.pluievolution.core.dao.acl.GeographicEtablissementDao;
import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.service.acl.GeographicEtablissementService;
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

    @Override
    public List<GeographicEtablissement> getAllEtablissement() {
        return geographicEtablissementMapper.entitiesToDto(geographicEtablissementDao.findAll());
    }
}
