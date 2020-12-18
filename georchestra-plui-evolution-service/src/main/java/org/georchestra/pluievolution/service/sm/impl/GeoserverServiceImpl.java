package org.georchestra.pluievolution.service.sm.impl;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.sm.GeoserverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.georchestra.pluievolution.service.common.constant.CommuneParams.CODE_INSEE_RM;

@Service
public class GeoserverServiceImpl implements GeoserverService {

    private static final Logger LOG = LoggerFactory.getLogger(GeoserverServiceImpl.class);

    @Value("${pluievolution.geoserver.url}")
    private String geoserverUrl;

    @Value("${pluievolution.geoserver.username}")
    private String geoserverUsername;

    @Value("${pluievolution.geoserver.password}")
    private String geoserverPassword;

    @Value("${pluievolution.geoserver.defaultworkspace}")
    private String geoserverWorkspace;

    static final String CODE_INSEE_COLUMN_NAME = "codeinsee";

    private static final String BASIC = "Basic ";

    private static final String GEOSERVER_CALQUE_ERROR = "Erreur lors de la récupération des informations sur le calque vers le GeoServer";
    private static final String GEOSERVER_REQUEST_ERROR = "Erreur lors de l'appel au GeoServer : ";
    private static final String GEOSERVER_WFS_FILTER_ERROR = "Erreur lors de la modification du flux WFS : ";


    @Override
    public InputStream getWms(GeographicArea area, String queryString, String contentType) throws ApiServiceException {

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            String wmsUrl = geoserverUrl + "/" + geoserverWorkspace +  "/wms";

            HttpGet httpGet = buildGeoserverHttpGet(wmsUrl, area, queryString, contentType);
            final HttpResponse response = httpClient.execute(httpGet);

            // Code 200 : succès
            if (response.getStatusLine().getStatusCode() == 200) {
                return IOUtils.toBufferedInputStream(response.getEntity().getContent());
            } else {
                throw new ApiServiceException(GEOSERVER_CALQUE_ERROR + response);
            }
        } catch (final IOException e) {
            throw new ApiServiceException(GEOSERVER_REQUEST_ERROR, e);
        }

    }

    @Override
    public String getWfs(GeographicArea area, String queryString) throws ApiServiceException {
        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            String wmsUrl = geoserverUrl + "/" + geoserverWorkspace +  "/wfs";

            HttpGet httpGet = buildGeoserverHttpGet(wmsUrl, area, queryString, MediaType.APPLICATION_JSON_VALUE);

            LOG.info("URL Get WFS {}", httpGet.getURI());

            final HttpResponse response = httpClient.execute(httpGet);

            return buildGeoserverWfsResponse(response);
        } catch (final IOException e) {
            throw new ApiServiceException(GEOSERVER_REQUEST_ERROR, e);
        }
    }

    @Override
    public String postWfs(GeographicArea area, String queryString, String wfsContent) throws ApiServiceException {

        try(CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            // Concatenation de l'URL geoserver avec les paramètres deja présent dans le requete original
            String wfsUrl = geoserverUrl + "/" + geoserverWorkspace +  "/wfs?" + URLDecoder.decode(queryString, StandardCharsets.UTF_8.displayName());

            // Filtre si l'utilisateur n'est pas un agent RM
            if (area != null && !area.getCodeInsee().equals(CODE_INSEE_RM)) {
                // filtre sur le code insee
                wfsContent = addFilterToWFSContent(wfsContent, area.getCodeInsee());
            }

            // paramètres d'authentification
            final String userpass = geoserverUsername + ":" + geoserverPassword;
            final String basicAuth = BASIC + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

            final HttpPost httpPost = new HttpPost(wfsUrl);
            httpPost.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, basicAuth);
            httpPost.setEntity(new StringEntity(wfsContent));

            LOG.info("URL Post WFS {}", httpPost.getURI());

            final HttpResponse response = httpClient.execute(httpPost);

            return buildGeoserverWfsResponse(response);
        } catch (final IOException e) {
            throw new ApiServiceException(GEOSERVER_REQUEST_ERROR, e);
        }
    }

    /**
     * Génration de la requête get pour geoserver
     * @param baseUrl                           url get
     * @param area                              geographic area pour filtrer
     * @param queryString                       paramètres de la requête get
     * @param contentType                       content type de la requête
     * @return                                  HttpGet
     * @throws UnsupportedEncodingException     problème lors de l'encodage de la requête
     */
    private HttpGet buildGeoserverHttpGet(String baseUrl, GeographicArea area, String queryString, String contentType) throws UnsupportedEncodingException {
        // Concatenation de l'URL geoserver avec les paramètres deja présent dans le requete original
        String urlGet = baseUrl + "?" + URLDecoder.decode(queryString, StandardCharsets.UTF_8.displayName());
        // Filtre sur le code insee si l'utilisateur n'est pas un agent RM
        if (area != null && !area.getCodeInsee().equals(CODE_INSEE_RM)) {
            urlGet += String.format("&cql_filter=%s=%s", CODE_INSEE_COLUMN_NAME, area.getCodeInsee());
        }

        // paramètres d'authentification
        final String userpass = geoserverUsername + ":" + geoserverPassword;
        final String basicAuth = BASIC + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

        // Concatenation de l'URL Fullmaps avec les paramètres deja présent dans le requete original
        final HttpGet httpGet = new HttpGet(urlGet);

        httpGet.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        httpGet.setHeader(HttpHeaders.AUTHORIZATION, basicAuth);

        return httpGet;
    }

    /**
     * Parsing de la réponse pour la requête WFS
     * @param httpResponse              réponse de la requête WFS
     * @return                          réponse parsée
     * @throws ApiServiceException      Erreur lors du parsing de la réponse
     */
    private String buildGeoserverWfsResponse(HttpResponse httpResponse) throws ApiServiceException {
        // Code 200 : succès
        if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 200) {
            // Renvoi du contenu JSON (String dans le contrôleur)
            final StringWriter writer = new StringWriter();
            try {
                IOUtils.copy(httpResponse.getEntity().getContent(), writer, StandardCharsets.UTF_8.displayName());
            } catch (IOException e) {
                throw new ApiServiceException(GEOSERVER_CALQUE_ERROR + httpResponse, e);
            }
            return writer.toString();
        } else {
            throw new ApiServiceException(GEOSERVER_CALQUE_ERROR + httpResponse);
        }
    }

    /**
     * Ajout d'un filtre ogc à la requête WFS
     * @param wfsContent                contenu xml de la requête
     * @param codeInsee                 code insee
     * @return                          requête filtrée
     * @throws ApiServiceException      Erreur lors de l'ajout du filtre
     */
    private String addFilterToWFSContent(String wfsContent, String codeInsee) throws ApiServiceException {

        try {
            String newWFS = wfsContent;
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);

            final DocumentBuilder builder = factory.newDocumentBuilder();
            final StringReader strReader = new StringReader(wfsContent);
            final InputSource is = new InputSource(strReader);
            final Document document = builder.parse(is);
            document.getDocumentElement().normalize();

            final Node wfsQueryNode = document.getElementsByTagName("wfs:Query").item(0);
            final Node filterNode = document.getElementsByTagName("ogc:Filter").item(0);

            if (wfsQueryNode != null) {
                if (filterNode != null) {
                    filterNode.appendChild(buildPropertyToEqualElement(document, codeInsee));
                }
                else {
                    wfsQueryNode.appendChild(buildFilterElement(document, codeInsee));
                }

                // Conversion du nouveau flux XML en string...
                final StringWriter writer = new StringWriter();
                final StreamResult result = new StreamResult(writer);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
                transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); //
                transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                final Transformer transformer = transformerFactory.newTransformer();
                final DOMSource source = new DOMSource(document);
                transformer.transform(source, result);
                newWFS = writer.toString();
            }

            return newWFS;
        } catch (final ParserConfigurationException | SAXException | TransformerException | IOException e) {
            throw new ApiServiceException(GEOSERVER_WFS_FILTER_ERROR, e);
        }

    }

    /**
     * Générer l'élement xml pour le fitlre wfs ogc:Filter
     * @param document      document
     * @param codeInsee     code insee
     * @return              element
     */
    private Element buildFilterElement(Document document, String codeInsee) {
        Element filterElement = document.createElement("ogc:Filter");
        filterElement.appendChild(buildPropertyToEqualElement(document, codeInsee));

        return filterElement;
    }

    /**
     * Générer l'élement xml pour le fitlre wfs ogc:PropertyToEqual
     * @param document      document
     * @param codeInsee     code insee
     * @return              element
     */
    private Element buildPropertyToEqualElement(Document document, String codeInsee) {
        Element propertyIsEqualToElement = document.createElement("ogc:PropertyIsEqualTo");

        Element propertyNameElement = document.createElement("ogc:PropertyName");
        propertyNameElement.appendChild(document.createTextNode(CODE_INSEE_COLUMN_NAME));

        Element literalElement = document.createElement("ogc:Literal");
        literalElement.appendChild(document.createTextNode(codeInsee));

        propertyIsEqualToElement.appendChild(propertyNameElement);
        propertyIsEqualToElement.appendChild(literalElement);

        return propertyIsEqualToElement;
    }
}
