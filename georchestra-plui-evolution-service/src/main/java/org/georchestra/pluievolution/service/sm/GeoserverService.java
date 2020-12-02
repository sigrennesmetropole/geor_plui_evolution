package org.georchestra.pluievolution.service.sm;

import com.vividsolutions.jts.geom.Geometry;
import org.georchestra.pluievolution.core.dto.FeatureCollection;

import java.io.IOException;

public interface GeoserverService {
    /**
     * Handle wfs request to geoserver
     * @param bbox
     * @param area
     * @return
     */
    FeatureCollection handleWfs(Geometry bbox, Geometry area) throws IOException;
}
