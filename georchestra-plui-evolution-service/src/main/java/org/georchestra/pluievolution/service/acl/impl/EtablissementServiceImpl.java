package org.georchestra.pluievolution.service.acl.impl;

import org.georchestra.pluievolution.core.dao.acl.EtablissementDao;
import org.georchestra.pluievolution.core.dto.Etablissement;
import org.georchestra.pluievolution.service.acl.EtablissementService;
import org.georchestra.pluievolution.service.mapper.EtablissementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtablissementServiceImpl implements EtablissementService {

    @Autowired
    EtablissementDao etablissementDao;

    @Autowired
    EtablissementMapper etablissementMapper;

    @Override
    public List<Etablissement> getAllEtablissement() {
        return etablissementMapper.entitiesToDto(etablissementDao.findAll());
    }
}
