package org.georchestra.pluievolution.api.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.georchestra.pluievolution.api.AdministrationApi;
import org.georchestra.pluievolution.core.dto.ConfigurationData;
import org.georchestra.pluievolution.service.sm.ConfigurationService;
import org.georchestra.pluievolution.service.sm.InitializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	/**
	 * point d'entrée utilisé uniquement en mode développement
	 * 
	 * @return une "js"
	 */
	@RequestMapping(value = "/extension/index.js", produces = { "application/javascript" }, method = RequestMethod.GET)
	public ResponseEntity<String> indexJs() {
		return ResponseEntity.ok("console.log(\"Sigm indexjs\");");
	}

	/**
	 * Point d'entrée utilisé uniquement en mode développement pour donner un accès
	 * de type proxy
	 * 
	 * @param url l'url de redirection
	 * @return
	 * @throws URISyntaxException
	 */
	@RequestMapping(value = "/proxy", method = RequestMethod.GET)
	public ResponseEntity<Void> proxy(@RequestParam(value = "url", required = false) String url)
			throws URISyntaxException {
		URI frontURI = new URI(url);
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(frontURI);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Redirect to {}", frontURI);
		}
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}
}
