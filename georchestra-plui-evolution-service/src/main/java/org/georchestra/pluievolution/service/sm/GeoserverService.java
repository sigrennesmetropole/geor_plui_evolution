package org.georchestra.pluievolution.service.sm;

import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.service.bean.GeoserverStream;
import org.georchestra.pluievolution.service.exception.ApiServiceException;

public interface GeoserverService {
    /**
     * Handle wms request to geoserver
     * @param area              element de filtre
     * @param encoding
     * @param queryString       paramètre de la requête url
     * @return                  Image WMS
     */
    GeoserverStream getWms(GeographicArea area,String encoding, String queryString, String contentType) throws ApiServiceException;

    /**
     * Requête WFS POST
     * @param area                      element de filtre
     * @param encoding					encoding
     * @param queryString               paramètre de la requête url
     * @param wfsContent                content de la requête post
     * @return                          résultat wfs
     * @throws ApiServiceException      Erreur lors de la requête WFS GET
     */
    String postWfs(GeographicArea area,String encoding,  String queryString, String wfsContent) throws ApiServiceException;

    /**
     * Requête WFS GET
     * @param area                      element de filtre
     * @param encoding
     * @param queryString               paramètre de la requête url
     * @return                          résultat wfs
     * @throws ApiServiceException      Erreur lors de la requête WFS GET
     */
    String getWfs( GeographicArea area,String encoding, String queryString) throws ApiServiceException;;
}
