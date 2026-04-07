package org.georchestra.pluievolution.api.controller;

import java.util.List;

import org.georchestra.pluievolution.api.GeographicApi;
import org.georchestra.pluievolution.core.dto.EtablissementConfiguration;
import org.georchestra.pluievolution.core.dto.GeographicArea;
import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.service.acl.GeographicAreaService;
import org.georchestra.pluievolution.service.acl.GeographicEtablissementService;
import org.georchestra.pluievolution.service.sm.ConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GeographicController implements GeographicApi {

	private final GeographicEtablissementService geographicEtablissementService;

	private final GeographicAreaService geographicAreaService;

	private final ConfigurationService configurationService;

	@Override
	public ResponseEntity<List<GeographicArea>> getAllPluiRequestAreas() throws Exception {
		return new ResponseEntity<>(geographicAreaService.getAllGeographicArea(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GeographicArea> getPluiRequestAreaByCodeInsee(String codeInsee) throws Exception {
		return new ResponseEntity<>(geographicAreaService.getGeographicAreaByCodeInsee(codeInsee), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<GeographicEtablissement> getPluiRequestEtablissementByCodeInsee(String codeInsee)
			throws Exception {
		return new ResponseEntity<>(geographicEtablissementService.getGeographicEtablissementByCodeInsee(codeInsee),
				HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<GeographicEtablissement>> searchEtablissements() throws Exception {
		return new ResponseEntity<>(geographicEtablissementService.searchEtablissements(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<EtablissementConfiguration> getEtablissementConfiguration() throws Exception {
		return new ResponseEntity<>(configurationService.getEtablissementConfiguration(), HttpStatus.OK);
	}
}
