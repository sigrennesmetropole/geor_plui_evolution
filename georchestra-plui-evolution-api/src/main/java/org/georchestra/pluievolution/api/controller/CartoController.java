package org.georchestra.pluievolution.api.controller;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.georchestra.pluievolution.api.CartoApi;
import org.georchestra.pluievolution.core.dto.LayerConfiguration;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.bean.GeoserverStream;
import org.georchestra.pluievolution.service.sm.ConfigurationService;
import org.georchestra.pluievolution.service.sm.GeoserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import io.swagger.annotations.Api;

/**
 * @author NCA20245
 *
 */
@RestController
@Api(tags = "carto")
public class CartoController implements CartoApi {

	@Autowired
	GeographicAreaService geographicAreaService;

	@Autowired
	GeoserverService geoserverService;

	@Autowired
	ConfigurationService configurationService;

	@Override
	public ResponseEntity<Void> getWms() throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getResponse();

		OutputStream out = response.getOutputStream();
		if ("GetMap".equals(request.getParameter("REQUEST"))) {
			GeoserverStream geoserverStream = geoserverService.getWms(geographicAreaService.getCurrentUserArea(),
					request.getCharacterEncoding(), request.getQueryString(), "application/img");
			if (geoserverStream != null && geoserverStream.getStream() != null) {
				BufferedImage bufferedImage = ImageIO.read(geoserverStream.getStream());
				if (bufferedImage != null) {
					ImageIO.write(bufferedImage, "png", out);
				}
			}
		} else {
			GeoserverStream geoserverStream = geoserverService.getWms(geographicAreaService.getCurrentUserArea(),
					request.getCharacterEncoding(), request.getQueryString(), "application/json");
			if (geoserverStream != null && geoserverStream.getStream() != null) {
				response.setContentType(geoserverStream.getMimeType());
				response.setCharacterEncoding("UTF-8");
				IOUtils.copy(geoserverStream.getStream(), response.getOutputStream());
			}
		}
		out.close();
		return null;
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
		return ResponseEntity.status(geoserverStream.getStatus()).contentType(MediaType.parseMediaType(geoserverStream.getMimeType())).body(geoserverStream.getContent());
	}

	@Override
	public ResponseEntity<String> postWfs(@Valid String body) throws Exception {
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
		GeoserverStream geoserverStream =geoserverService.postWfs(geographicAreaService.getCurrentUserArea(),
				request.getCharacterEncoding(), wfsQueryString, wfsContent);
		return ResponseEntity.status(geoserverStream.getStatus()).contentType(MediaType.parseMediaType(geoserverStream.getMimeType())).body(geoserverStream.getContent());
	}

}
