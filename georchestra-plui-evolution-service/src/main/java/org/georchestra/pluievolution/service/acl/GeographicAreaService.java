package org.georchestra.pluievolution.service.acl;

import org.georchestra.pluievolution.core.dto.GeographicArea;

import java.util.List;

public interface GeographicAreaService {
    /**
     * Permet de recuperer la liste de toutes les geographiques area
     * @return
     */
    List<GeographicArea> getAllGeographicArea();
}
