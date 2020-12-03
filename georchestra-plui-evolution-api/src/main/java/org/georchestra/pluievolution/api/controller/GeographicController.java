package org.georchestra.pluievolution.api.controller;

import io.swagger.annotations.Api;
import org.georchestra.pluievolution.api.GeographicApi;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.acl.GeographicEtablissementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "geographic")
public class GeographicController implements GeographicApi {

    @Autowired
    GeographicEtablissementService geographicEtablissementService;

    @Autowired
    GeographicAreaService geographicAreaService;

    @Override
    public ResponseEntity<List<GeographicArea>> getAllPluiRequestAreas() throws Exception {
        return new ResponseEntity<>(geographicAreaService.getAllGeographicArea(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<GeographicEtablissement>> getAllPluiRequestEtablissements() throws Exception {
        return new ResponseEntity<>(geographicEtablissementService.getAllEtablissement(), HttpStatus.OK);
    }
}
