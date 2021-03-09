package org.georchestra.pluievolution.api.controller;

import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.georchestra.pluievolution.api.CartoApi;
import org.georchestra.pluievolution.core.dto.LayerConfiguration;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.sm.ConfigurationService;
import org.georchestra.pluievolution.service.sm.GeoserverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;

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
           InputStream stream = geoserverService.getWms(geographicAreaService.getCurrentUserArea(), request.getQueryString(), "application/img");
            if (stream != null) {
                BufferedImage bufferedImage = ImageIO.read(stream);
                if (bufferedImage != null) {
                    ImageIO.write(bufferedImage, "png", out);
                }
            }
        } else {
            InputStream stream = geoserverService.getWms(geographicAreaService.getCurrentUserArea(), request.getQueryString(), "application/json");
            if (stream != null) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                IOUtils.copy(stream, response.getOutputStream());

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

        String json = geoserverService.getWfs(geographicAreaService.getCurrentUserArea(), request.getQueryString());
        return ResponseEntity.ok(json);
    }

    @Override
    public ResponseEntity<String> postWfs(@Valid String wfsContent) throws Exception {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        String json = geoserverService.postWfs(geographicAreaService.getCurrentUserArea(), request.getQueryString(), wfsContent);
        return ResponseEntity.ok(json);
    }

}
