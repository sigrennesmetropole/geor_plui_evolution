package org.georchestra.pluievolution.service.sm.impl;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.sm.GeoserverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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

    private static final String GEOSERVER_CALQUE_ERROR = "Erreur lors de la rcupération du calque vers le GeoServer";
    private static final String GEOSERVER_REQUEST_ERROR = "Erreur lors de l'appel au GeoServer : ";


    @Override
    public InputStream getWms(GeographicArea area, String queryString, String contentType) throws ApiServiceException {

        CloseableHttpClient httpClient = null;

        try {
            httpClient = HttpClientBuilder.create().build();

            String wmsUrl = geoserverUrl + "/wms?" + URLDecoder.decode(queryString, StandardCharsets.UTF_8.displayName());
            if (area != null) {
                wmsUrl += String.format("&cql_filter=%s=%s", CODE_INSEE_COLUMN_NAME, area.getCodeInsee());
            }

            // Concatenation de l'URL Fullmaps avec les paramètres deja présent dans le requete original
            final HttpGet httpGet = new HttpGet(wmsUrl);

            final String userpass = geoserverUsername + ":" + geoserverPassword;
            final String basicAuth = BASIC + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, basicAuth);
            final HttpResponse response = httpClient.execute(httpGet);

            // Code 200 : succès
            if (response.getStatusLine().getStatusCode() == 200) {
                return IOUtils.toBufferedInputStream(response.getEntity().getContent());
            } else {
                throw new ApiServiceException(GEOSERVER_CALQUE_ERROR);
            }
        } catch (final IOException e) {
            throw new ApiServiceException(GEOSERVER_REQUEST_ERROR, e);
        } finally {
            // On essaie toujours de fermer le client HTTP après son ouverture
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                // Si on arrive pas à fermer le client on log une erreur
            } catch (final IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }


    }
}
