package org.georchestra.pluievolution.service.sm;

import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.service.exception.ApiServiceException;

import java.io.InputStream;

public interface GeoserverService {
    /**
     * Handle wms request to geoserver
     * @param area              element de filtre
     * @param queryString       paramètre de la requête url
     * @return                  Image WMS
     */
    InputStream getWms(GeographicArea area, String queryString, String contentType) throws ApiServiceException;

    /**
     * Requête WFS POST
     * @param area                      element de filtre
     * @param queryString               paramètre de la requête url
     * @param wfsContent                content de la requête post
     * @return                          résultat wfs
     * @throws ApiServiceException      Erreur lors de la requête WFS GET
     */
    String postWfs(GeographicArea area, String queryString, String wfsContent) throws ApiServiceException;

    /**
     * Requête WFS GET
     * @param area                      element de filtre
     * @param queryString               paramètre de la requête url
     * @return                          résultat wfs
     * @throws ApiServiceException      Erreur lors de la requête WFS GET
     */
    String getWfs(GeographicArea area, String queryString) throws ApiServiceException;;
}
