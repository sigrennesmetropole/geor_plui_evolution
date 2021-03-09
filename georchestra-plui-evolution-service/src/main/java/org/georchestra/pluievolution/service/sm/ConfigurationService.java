package org.georchestra.pluievolution.service.sm;

import org.georchestra.pluievolution.core.dto.ConfigurationData;
import org.georchestra.pluievolution.core.dto.EtablissementConfiguration;
import org.georchestra.pluievolution.core.dto.LayerConfiguration;

/**
 * Interface du service de configuration.
 */
public interface ConfigurationService {

    /**
     * Lecture de la version de l'application.
     *
     * @return version
     */
	ConfigurationData getApplicationVersion();

    /**
     * Permet d'obtenir les informations relatives à la couche des pluirequest dans le geoserver
     * @return
     */
	LayerConfiguration getLayerConfigurations();

    /**
     * @return Configuration des établissements
     */
	EtablissementConfiguration getEtablissementConfiguration();
}
