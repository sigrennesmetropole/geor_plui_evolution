package org.georchestra.pluievolution.api.controller;

import org.georchestra.pluievolution.api.AdministrationApi;
import org.georchestra.pluievolution.core.dto.ConfigurationData;
import org.georchestra.pluievolution.service.sm.ConfigurationService;
import org.georchestra.pluievolution.service.sm.InitializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-06-12T10:28:09.523+02:00")

/**
 * Controlleur pour la configuration.
 */
@RestController
@Api(tags = "administration")
public class AdministrationController implements AdministrationApi {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdministrationController.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private InitializationService initializationService;

	@Override
	public ResponseEntity<ConfigurationData> getConfiguration() throws Exception {
		return new ResponseEntity<ConfigurationData>(configurationService.getApplicationVersion(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Boolean> initialize() throws Exception {
		try {
			initializationService.initialize();
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			LOGGER.warn("Failed to initialize...", e);
			return ResponseEntity.ok(false);
		}
	}
}
