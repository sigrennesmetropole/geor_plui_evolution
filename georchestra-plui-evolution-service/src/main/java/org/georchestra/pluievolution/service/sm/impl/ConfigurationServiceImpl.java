package org.georchestra.pluievolution.service.sm.impl;

import org.georchestra.pluievolution.core.dto.LayerConfiguration;
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

    @Value("${pluievolution.geoserver.defaultworkspace}")
    private String layerWorkspace;

    @Value("${pluievolution.geoserver.layerName}")
    private String layerName;

    @Value("${pluievolution.geoserver.layerSRID}")
    private int layerSRID;

    @Autowired
    ConfigurationMapper configMapper;

    @Override
    public ConfigurationData getApplicationVersion() {
        return configMapper.entityToDto(new Configuration(version));
    }

    @Override
    public LayerConfiguration getLayerConfigurations() {
        LayerConfiguration layerConfiguration = new LayerConfiguration();
        layerConfiguration.setLayerName(layerName);
        layerConfiguration.setLayerWorkspace(layerWorkspace);
        layerConfiguration.setLayerProjection("EPSG:" + layerSRID);
        return layerConfiguration;
    }
}
