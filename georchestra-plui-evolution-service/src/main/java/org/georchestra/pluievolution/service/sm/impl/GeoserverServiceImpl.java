package org.georchestra.pluievolution.service.sm.impl;

import static org.georchestra.pluievolution.service.common.constant.CommuneParams.CODE_INSEE_RM;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.service.bean.GeoserverStream;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.exception.ApiServiceExceptionsStatus;
import org.georchestra.pluievolution.service.sm.GeoserverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Service
public class GeoserverServiceImpl implements GeoserverService {

	private static final Logger LOG = LoggerFactory.getLogger(GeoserverServiceImpl.class);

	private static final String CODE_INSEE_COLUMN_NAME = "codeinsee";

	private static final String SERVICE_WMTS = "service=wmts";

	private static final String BASIC = "Basic ";

	private static final String GEOSERVER_CALQUE_ERROR = "Erreur lors de la récupération des informations sur le calque vers le GeoServer";
	private static final String GEOSERVER_REQUEST_ERROR = "Erreur lors de l'appel au GeoServer : ";
	private static final String GEOSERVER_SERVICE_ERROR = "Service unavailable";
	private static final String GEOSERVER_WFS_FILTER_ERROR = "Erreur lors de la modification du flux WFS : ";

	private static final String CQL_FILTER_PARAM_NAME = "cql_filter";

	@Value("${pluievolution.geoserver.url}")
	private String geoserverUrl;

	@Value("${pluievolution.geoserver.username}")
	private String geoserverUsername;

	@Value("${pluievolution.geoserver.password}")
	private String geoserverPassword;

	@Value("${pluievolution.geoserver.defaultworkspace}")
	private String geoserverWorkspace;

	@Value("${pluievolution.geoserver.enableWmts:false}")
	private boolean enableWmts;

	@Override
	public GeoserverStream getWms(GeographicArea area, String encoding, String queryString, String contentType)
			throws ApiServiceException {

		if (!enableWmts && queryString != null && queryString.toLowerCase().contains(SERVICE_WMTS)) {
			LOG.warn("{} {}", GEOSERVER_SERVICE_ERROR, "Erreur WMTS");
			throw new ApiServiceException(GEOSERVER_SERVICE_ERROR, ApiServiceExceptionsStatus.BAD_REQUEST);
		}
		try (CloseableHttpClient httpClient = createHttpClient()) {

			String wmsUrl = buildURL("wms");
			HttpGet httpGet = buildGeoserverHttpGet(wmsUrl, area, queryString, contentType, encoding);
			LOG.info("URL Get WMS {}", httpGet.getURI());
			final HttpResponse response = httpClient.execute(httpGet);

			// Code 200 : succès
			String outputContentType = extractContentType(response, contentType);
			if (response.getStatusLine().getStatusCode() == 200 && !StringUtils.contains(outputContentType, "text/xml")) {
				return GeoserverStream.builder().status(response.getStatusLine().getStatusCode())
						.stream(IOUtils.toBufferedInputStream(response.getEntity().getContent()))
						.mimeType(outputContentType).build();
			} else {
				LOG.error("getWms calque {} {} {}",response.getStatusLine(), outputContentType, httpGet.getURI());
				throw new ApiServiceException(String.format("%s %s", GEOSERVER_CALQUE_ERROR, response),
						ApiServiceExceptionsStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			LOG.error("getWms call",e);
			throw new ApiServiceException(String.format("%s %s", GEOSERVER_REQUEST_ERROR, e.getMessage()), e, ApiServiceExceptionsStatus.BAD_REQUEST);
		}

	}

	private String extractContentType(final HttpResponse response, String defaultValue) {
		Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
		String outputContentType = defaultValue;
		if (header != null && StringUtils.isNotEmpty(header.getValue())) {
			outputContentType = header.getValue();
		}
		if (StringUtils.isEmpty(outputContentType)) {
			outputContentType = defaultValue;
		}
		return outputContentType;
	}

	@Override
	public GeoserverStream getWfs(GeographicArea area, String encoding, String queryString) throws ApiServiceException {
		try (CloseableHttpClient httpClient = createHttpClient()) {

			String wfsUrl = buildURL("wfs");
			HttpGet httpGet = buildGeoserverHttpGet(wfsUrl, area, queryString, MediaType.APPLICATION_JSON_VALUE,
					encoding);

			LOG.info("URL Get WFS {}", httpGet.getURI());

			final HttpResponse response = httpClient.execute(httpGet);

			return buildGeoserverWfsResponse(response);
		} catch (final Exception e) {
			LOG.error("getWfs call",e);
			throw new ApiServiceException(GEOSERVER_REQUEST_ERROR + e.getMessage(), e);
		}
	}

	@Override
	public GeoserverStream postWfs(GeographicArea area, String encoding, String queryString, String wfsContent)
			throws ApiServiceException {

		try (CloseableHttpClient httpClient = createHttpClient()) {

			// Concatenation de l'URL geoserver avec les paramètres deja présent dans le
			// requete original
			String wfsUrl = buildURL("wfs?" + buildQuery(queryString, encoding));

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
		} catch (final Exception e) {
			LOG.error("postWfs call",e);
			throw new ApiServiceException(GEOSERVER_REQUEST_ERROR, e);
		}
	}

	/**
	 * Génration de la requête get pour geoserver
	 *
	 * @param baseUrl     url get
	 * @param area        geographic area pour filtrer
	 * @param queryString paramètres de la requête get
	 * @param contentType content type de la requête
	 * @return HttpGet
	 */
	@Override
	public HttpGet buildGeoserverHttpGet(String baseUrl, GeographicArea area, String queryString, String contentType,
			String encoding) throws ApiServiceException {
		// Concatenation de l'URL geoserver avec les paramètres deja présent dans le
		queryString = buildQuery(queryString, encoding);
		try {

			// requete original
			String urlGet = baseUrl + "?" + queryString;
			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(urlGet);
			Pair<String, String> cql = getQueryParam(CQL_FILTER_PARAM_NAME, queryString);
			String filter = cql != null ? cql.getSecond() : "";
			// Filtre sur le code insee si l'utilisateur n'est pas un agent RM
			// Ce nouveau ne doit pas se substituer aux filtres deja present...
			// Si un filtre est deja present, le nouveau filtre s'y ajoute avec la condition AND
			// Si l'utilisateur est un agent RM, le filtre si existant dans l'URL reste tel quel
			if (area != null && !area.getCodeInsee().equals(CODE_INSEE_RM)) {
				if (StringUtils.isNotEmpty(filter)) {
					filter = String.format("(%s) AND ", filter);
				}
				filter += String.format("%s='%s'", CODE_INSEE_COLUMN_NAME, area.getCodeInsee());

				// On met à jour notre URL avec le filtre

				uriBuilder.replaceQueryParam(cql != null ? cql.getFirst() : CQL_FILTER_PARAM_NAME, filter);

			}


			// paramètres d'authentification
			final String userpass = geoserverUsername + ":" + geoserverPassword;
			final String basicAuth = BASIC + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

			// On met à jour l'URL
			urlGet = uriBuilder.build().encode().toUriString();

			// Concatenation de l'URL Fullmaps avec les paramètres deja présent dans le
			// requete original
			final HttpGet httpGet = new HttpGet(urlGet);

			httpGet.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
			httpGet.setHeader(HttpHeaders.AUTHORIZATION, basicAuth);
			return httpGet;
		} catch (Exception e) {
			throw new ApiServiceException("Une erreur a été pendant la mise à jour du filtre CQL: " + e.getMessage());
		}

	}

	/**
	 * Permet de recuperer une query param sous la forme de clé valeur
	 * @param name			nom de la queryParam
	 * @param queryString	chaine de caracteres des params
	 * @return une paire (nom, valeur)
	 */
	private Pair<String, String> getQueryParam(String name, String queryString) {
		if (StringUtils.isNotEmpty(queryString) && StringUtils.isNotEmpty(name)) {
			List<String> queries = List.of(queryString.split("&"));
			for (String query : queries) {
				if (query.toLowerCase().startsWith(name.toLowerCase().trim())) {
					return query.contains("=") ? Pair.of(query.split("=")[0], query.substring(query.indexOf("=") + 1)) : null;
				}
			}
		}
		return null;
	}

	/**
	 * Parsing de la réponse pour la requête WFS
	 *
	 * @param httpResponse réponse de la requête WFS
	 * @return réponse parsée
	 * @throws ApiServiceException Erreur lors du parsing de la réponse
	 */
	private GeoserverStream buildGeoserverWfsResponse(HttpResponse httpResponse) throws ApiServiceException {
		// Code 200 : succès
		if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() >= 200
				&& httpResponse.getStatusLine().getStatusCode() <= 302) {
			// Renvoi du contenu JSON (String dans le contrôleur)
			LOG.info("Wfs received {}", httpResponse.getStatusLine());
			String outputContentType = extractContentType(httpResponse, MediaType.APPLICATION_JSON_VALUE);
			LOG.info("Wfs contenttype {}", outputContentType);

			if (httpResponse.getStatusLine().getStatusCode() < 300) {
				final StringWriter writer = new StringWriter();
				try {
					IOUtils.copy(httpResponse.getEntity().getContent(), writer, StandardCharsets.UTF_8.displayName());
					LOG.info("Wfs copy {}", writer);
					return GeoserverStream.builder().content(writer.toString())
							.status(httpResponse.getStatusLine().getStatusCode()).mimeType(outputContentType).build();
				} catch (IOException e) {
					throw new ApiServiceException(GEOSERVER_CALQUE_ERROR + httpResponse, e);
				}
			} else {
				for (Header header : httpResponse.getAllHeaders()) {
					LOG.info("header {}", header);
				}
				return GeoserverStream.builder().status(httpResponse.getStatusLine().getStatusCode())
						.mimeType(outputContentType).build();
			}

		} else {
			throw new ApiServiceException(GEOSERVER_CALQUE_ERROR + httpResponse);
		}
	}

	/**
	 * Ajout d'un filtre ogc à la requête WFS
	 *
	 * @param wfsContent contenu xml de la requête
	 * @param codeInsee  code insee
	 * @return requête filtrée
	 * @throws ApiServiceException Erreur lors de l'ajout du filtre
	 */
	private String addFilterToWFSContent(String wfsContent, String codeInsee) throws ApiServiceException {

		try {
			String newWFS = "";
			if (!wfsContent.startsWith("<?xml")) {
				newWFS = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			}
			newWFS += wfsContent;
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
					// On applique un operateur AND entre les filtre existants et le nouveau
					NodeList filterChilds = filterNode.getChildNodes();
					Node andOperator = buildAndFilterOperatorElement(document);
					for (int i = 0; i < filterChilds.getLength(); i++) {
						andOperator.appendChild(filterChilds.item(i));
					}
					andOperator.appendChild(buildPropertyToEqualElement(document, codeInsee));
					filterNode.appendChild(andOperator);
				} else {
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
	 *
	 * @param document  document
	 * @param codeInsee code insee
	 * @return element
	 */
	private Element buildFilterElement(Document document, String codeInsee) {
		Element filterElement = document.createElement("ogc:Filter");
		filterElement.appendChild(buildPropertyToEqualElement(document, codeInsee));

		return filterElement;
	}

	/**
	 * Générer l'operateur AND xml pour le fitlre wfs et permettre de combiner les differents filtres
	 *
	 * @param document  document
	 * @return element
	 */
	private Element buildAndFilterOperatorElement(Document document) {
		return document.createElement("ogc:And");
	}

	/**
	 * Générer l'élement xml pour le fitlre wfs ogc:PropertyToEqual
	 *
	 * @param document  document
	 * @param codeInsee code insee
	 * @return element
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

	private static CloseableHttpClient createHttpClient()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		// use the TrustSelfSignedStrategy to allow Self Signed Certificates
		SSLContext sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();

		// we can optionally disable hostname verification.
		// if you don't want to further weaken the security, you don't have to include
		// this.
		HostnameVerifier allowAllHosts = new NoopHostnameVerifier();

		// create an SSL Socket Factory to use the SSLContext with the trust self signed
		// certificate strategy
		// and allow all hosts verifier.
		SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);

		// finally create the HttpClient using HttpClient factory methods and assign the
		// ssl socket factory
		return HttpClients.custom().setSSLSocketFactory(connectionFactory).build();
	}

	private String buildURL(String service) {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(geoserverUrl).append('/').append(geoserverWorkspace);
		if (!service.startsWith("/")) {
			urlBuilder.append('/');
		}
		urlBuilder.append(service);
		return urlBuilder.toString();
	}

	private String buildQuery(String query, String encoding) throws ApiServiceException {
		try {
			if (StringUtils.isNotEmpty(query)) {
				return URLDecoder.decode(query, Charset.forName(encoding));
			}
			return StringUtils.EMPTY;
		} catch (Exception e) {
			throw new ApiServiceException("Une erreur recontrée pendant le décodage de l'url " + e.getMessage(), e);
		}
	}
}
