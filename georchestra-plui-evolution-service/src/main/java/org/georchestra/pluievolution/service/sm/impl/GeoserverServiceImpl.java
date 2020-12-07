package org.georchestra.pluievolution.service.sm.impl;

import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.sm.GeoserverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLDecoder;

@Service
public class GeoserverServiceImpl implements GeoserverService {

    private static final Logger LOG = LoggerFactory.getLogger(GeoserverServiceImpl.class);

    @Value("${pluievolution.geoserver.url}")
    private String geoserverUrl;

    @Value("${pluievolution.geoserver.username}")
    private String geoserverUsername;

    @Value("${pluievolution.geoserver.password}")
    private String geoserverPassword;

    static final String CODE_INSEE_COLUMN_NAME = "codeinsee";

    private static final String BASIC = "Basic ";

    private static final String AUTHORIZATION = "Authorization";

    private static final String GEOSERVER_CALQUE_ERROR = "Erreur lors de la rcupération du claque vers le GeoServer";
    private static final String GEOSERVER_REQUEST_ERROR = "Erreur lors de l'appel au GeoServer : ";


    @Override
    public InputStream getWms(GeographicArea area, String queryString) {

        String filterString = null;
        if (area != null) {
            filterString = String.format("&cql_filter=%s = %s", CODE_INSEE_COLUMN_NAME, area.getCodeInsee());
        }


        HttpURLConnection connection = null;
        try {
            // Construction de l'URL
            String requestString = geoserverUrl + "/wms?" + URLDecoder.decode(queryString);
            if (filterString != null) {
                requestString+= filterString;
            }
            final UriBuilder uriB = UriComponentsBuilder.fromHttpUrl(requestString);


            final String userpass = geoserverUsername + ":" + geoserverPassword;
            final String basicAuth = BASIC + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            connection = (HttpURLConnection) uriB.build().toURL().openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty(AUTHORIZATION, basicAuth);



            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return connection.getInputStream();
            } else {

                LOG.error(String.format("%s: %s", GEOSERVER_CALQUE_ERROR, connection.getResponseCode()));
                throw new ApiServiceException(GEOSERVER_CALQUE_ERROR);
            }


        } catch (final IOException e) {
            LOG.error(GEOSERVER_REQUEST_ERROR, e);
        } catch (final Exception e) {
            LOG.error(GEOSERVER_REQUEST_ERROR, e);
        }
        return null;
    }
}
