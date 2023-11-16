/**
 * 
 */
package org.georchestra.pluievolution.api.controller;

import org.georchestra.pluievolution.api.UserApi;
import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.core.dto.User;
import org.georchestra.pluievolution.service.acl.GeographicEtablissementService;
import org.georchestra.pluievolution.service.sm.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FNI18300
 *
 */
@RestController
public class UserController implements UserApi {

	@Autowired
	private UserService userService;

	@Autowired
	private GeographicEtablissementService geographicEtablissementService;

	@Override
	public ResponseEntity<GeographicEtablissement> getCurrentUserEtablissement() throws Exception {
		return new ResponseEntity<>(geographicEtablissementService.getCurrentUserEtablissement(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<User> getMe() throws Exception {
		return ResponseEntity.ok(userService.getMe());
	}
}
