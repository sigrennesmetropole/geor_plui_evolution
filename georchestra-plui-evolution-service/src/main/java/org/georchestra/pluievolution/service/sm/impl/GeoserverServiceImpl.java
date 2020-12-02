package org.georchestra.pluievolution.service.sm.impl;

import com.vividsolutions.jts.geom.Geometry;
import org.georchestra.pluievolution.service.mapper.FeatureMapper;
import org.geotools.feature.FeatureCollection;
import org.georchestra.pluievolution.core.dto.PluiRequestType;
import org.georchestra.pluievolution.service.sm.GeoserverService;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.impl.WFSDataAccessFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureIterator;
import org.geotools.util.Version;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.spatial.Intersects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
public class GeoserverServiceImpl implements GeoserverService {

    @Autowired
    FeatureMapper featureMapper;

    @Value("${pluievolution.geoserver.url}")
    private String geoserverUrl;

    @Value("${pluievolution.geoserver.username}")
    private String geoserverUsername;

    @Value("${pluievolution.geoserver.password}")
    private String geoserverPassword;

    @Value("${pluievolution.geoserver.defaultworkspace}")
    private String geoserverDefaultWorkspace;

    static final String GEOMETRY_COLUMN_NAME = "geometry";

    static final String TYPE_COLUMN_NAME = "type";


    @Override
    public org.georchestra.pluievolution.core.dto.FeatureCollection handleWfs(Geometry bbox, Geometry area) throws IOException {
        // Parametres de connexion
        String getCapabilities = WFSDataStoreFactory.createGetCapabilitiesRequest(new URL(String.format("%s/wfs", geoserverUrl)), new Version("1.1.0")).toString();
        Map<String, String> connectionParameters = new HashMap<>();
        connectionParameters.put(WFSDataAccessFactory.URL.key, getCapabilities);
        connectionParameters.put(WFSDataAccessFactory.PASSWORD.key, geoserverPassword);
        connectionParameters.put(WFSDataAccessFactory.USERNAME.key, geoserverUsername);

        // Connection
        DataStore data = DataStoreFinder.getDataStore(connectionParameters);

        // Layer
        String typeName = String.format("%s:plui_request", geoserverDefaultWorkspace);
        SimpleFeatureSource source = data.getFeatureSource(typeName);

        FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2( GeoTools.getDefaultHints() );

        // On initialise la liste des filtres
        List<Filter> filters = new ArrayList<>();

        // Filtre d'Intersection avec le boundingbox de la carte
        Intersects filter1 = filterFactory.intersects(filterFactory.property(GEOMETRY_COLUMN_NAME), filterFactory.literal(bbox));
        filters.add(filter1);

        // Intersection avec l'ère geographique accessible à l'utilisateur courant
        // area est nul si agent Rennes Metropole
        if (area != null) {
            Intersects filter2 = filterFactory.intersects(filterFactory.property(GEOMETRY_COLUMN_NAME), filterFactory.literal(area));
            // on exclu les demandes metropolitaines car agent pas RM
            PropertyIsNotEqualTo filter3 = filterFactory.notEqual(filterFactory.property(TYPE_COLUMN_NAME), filterFactory.literal(PluiRequestType.METROPOLITAIN));
            filters.add(filter2);
            filters.add(filter3);
        }

        // On associe les differents filtres avec un AND operator
        Filter filter = filterFactory.and(filters);

        // Creation de la requete
        Query query = new Query(typeName, filter, new String[]{GEOMETRY_COLUMN_NAME});

        // Execution de la requete
        FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures( query );

        // On cree la collection de retour dans laquelle nous allons mapper les features recues
        org.georchestra.pluievolution.core.dto.FeatureCollection fc = new org.georchestra.pluievolution.core.dto.FeatureCollection();
        fc.setType(org.georchestra.pluievolution.core.dto.FeatureCollection.TypeEnum.FEATURECOLLECTION);

        try (FeatureIterator<SimpleFeature> iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                fc.addFeaturesItem(featureMapper.simpleFeatureToFeature(feature));
            }
        }

        return fc;
    }
}
