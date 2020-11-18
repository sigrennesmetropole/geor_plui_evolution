package org.georchestra.pluievolution.service.sm;

import org.georchestra.pluievolution.core.dto.ConfigurationData;

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
}
