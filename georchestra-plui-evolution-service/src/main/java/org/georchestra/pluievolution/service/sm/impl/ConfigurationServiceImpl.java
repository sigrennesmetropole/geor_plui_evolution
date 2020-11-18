package org.georchestra.pluievolution.service.sm.impl;

import org.georchestra.pluievolution.service.bean.Configuration;
import org.georchestra.pluievolution.core.dto.ConfigurationData;
import org.georchestra.pluievolution.service.mapper.ConfigurationMapper;
import org.georchestra.pluievolution.service.sm.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    @Value("${application.version}")
    private String version;

    @Autowired
    ConfigurationMapper configMapper;

    @Override
    public ConfigurationData getApplicationVersion() {
        return configMapper.entityToDto(new Configuration(version));
    }
}
