package org.georchestra.pluievolution.service.sm;

import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.service.exception.ApiServiceException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;

public interface GeoserverService {
    /**
     * Handle wfs request to geoserver
     * @param area
     * @param queryString
     * @return
     */
    InputStream getWms(GeographicArea area, String queryString, String contentType) throws ApiServiceException;
}
