package org.georchestra.pluievolution.api.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.georchestra.pluievolution.api.CartoApi;
import org.georchestra.pluievolution.core.dto.LayerConfiguration;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.bean.GeoserverStream;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.sm.ConfigurationService;
import org.georchestra.pluievolution.service.sm.GeoserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author NCA20245
 *
 */
@RestController
public class CartoController implements CartoApi {

	@Autowired
	GeographicAreaService geographicAreaService;

	@Autowired
	GeoserverService geoserverService;

	@Autowired
	ConfigurationService configurationService;

	private static final String GET_MAP_REQUEST_PARAM_VALUE = "GetMap";
	private static final String REQUEST_PARAM = "REQUEST";

	@Override
	public ResponseEntity<Void> getWms() throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getResponse();

		if (response != null) {
			OutputStream out = response.getOutputStream();
			if (GET_MAP_REQUEST_PARAM_VALUE.equalsIgnoreCase(getParameterIgnoreCase(request, REQUEST_PARAM))) {
				getWmsImage(request, out);
			} else {
				getWmsOther(request, response);
			}
			out.close();
		}

		return null;
	}

	/**
	 * Traitement du getWms si request de type GETMAP
	 * @param request
	 * @param out
	 * @throws Exception
	 */
	private void getWmsImage(HttpServletRequest request, OutputStream out) throws ApiServiceException, IOException {
		GeoserverStream geoserverStream = geoserverService.getWms(geographicAreaService.getCurrentUserArea(),
				request.getCharacterEncoding(), request.getQueryString(), "image/png");
		if (geoserverStream != null && geoserverStream.getStream() != null) {
			BufferedImage bufferedImage = ImageIO.read(geoserverStream.getStream());
			if (bufferedImage != null) {
				ImageIO.write(bufferedImage, "png", out);
			}
		}
	}

	/**
	 * Traitement du getWms si autre type de requete
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getWmsOther(HttpServletRequest request, HttpServletResponse response) throws ApiServiceException, IOException {
		GeoserverStream geoserverStream = geoserverService.getWms(geographicAreaService.getCurrentUserArea(),
				request.getCharacterEncoding(), request.getQueryString(), "application/json");
		if (geoserverStream != null && geoserverStream.getStream() != null) {
			response.setContentType(geoserverStream.getMimeType());
			response.setCharacterEncoding("UTF-8");
			IOUtils.copy(geoserverStream.getStream(), response.getOutputStream());
		}
	}

	@Override
	public ResponseEntity<LayerConfiguration> getLayerConfigurations() throws Exception {
		return new ResponseEntity<>(configurationService.getLayerConfigurations(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> getWfs() throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();

		GeoserverStream geoserverStream = geoserverService.getWfs(geographicAreaService.getCurrentUserArea(),
				request.getCharacterEncoding(), request.getQueryString());
		return ResponseEntity.status(geoserverStream.getStatus())
				.contentType(getMediatTypeFromGeoserverStream(geoserverStream))
				.body(geoserverStream.getContent());
	}

	@Override
	public ResponseEntity<String> postWfs(String body) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();

		String wfsQueryString = request.getQueryString();
		String wfsContent = "";
		if (body != null) {
			String[] parts = body.split("&");
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].startsWith("<")) {
					wfsContent = parts[i];
				} else if (parts[i].startsWith("%")) {
					wfsContent = URLDecoder.decode(parts[i], request.getCharacterEncoding());
				}
			}
		}
		GeoserverStream geoserverStream = geoserverService.postWfs(geographicAreaService.getCurrentUserArea(),
				request.getCharacterEncoding(), wfsQueryString, wfsContent);
		return ResponseEntity.status(geoserverStream.getStatus())
				.contentType(getMediatTypeFromGeoserverStream(geoserverStream))
				.body(geoserverStream.getContent());
	}

	/**
	 * Les parametres transmis ont une casse differente selon la plateforme
	 * @param request	La requete
	 * @param paramName	Le nom du parametre recherché
	 * @return	Valeur du parametre
	 */
	private String getParameterIgnoreCase(HttpServletRequest request, String paramName) {
		Iterator<String> it = request.getParameterNames().asIterator();
		while (it.hasNext()) {
			String someParam = it.next();
			if (StringUtils.equalsIgnoreCase(someParam, paramName)) {
				return request.getParameter(someParam);
			}
		}
		return null;
	}

	/**
	 * Permet d'obtenir le mediatype à pa
	 * @param stream	Reponse du geoserver
	 * @return
	 */
	private MediaType getMediatTypeFromGeoserverStream(GeoserverStream stream) {
		MediaType mt;
		if (stream != null) {
			String mimetype = stream.getMimeType();
			if (mimetype.contains(";")) {
				mimetype = mimetype.split(";")[0];
			}
			try {
				mt = MediaType.parseMediaType(mimetype);
			} catch (Exception e) {
				mt = MediaType.TEXT_PLAIN;
			}
			return mt;
		}
		return MediaType.TEXT_PLAIN;
	}

}
