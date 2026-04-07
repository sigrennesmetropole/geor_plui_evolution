/**
 * 
 */
package org.georchestra.pluievolution.api.controller;

import org.georchestra.pluievolution.api.UserApi;
import org.georchestra.pluievolution.core.dto.GeographicEtablissement;
import org.georchestra.pluievolution.core.dto.User;
import org.georchestra.pluievolution.service.acl.GeographicEtablissementService;
import org.georchestra.pluievolution.service.sm.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * @author FNI18300
 *
 */
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

	private final UserService userService;

	private final GeographicEtablissementService geographicEtablissementService;

	@Override
	public ResponseEntity<GeographicEtablissement> getCurrentUserEtablissement() throws Exception {
		return new ResponseEntity<>(geographicEtablissementService.getCurrentUserEtablissement(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<User> getMe() throws Exception {
		return ResponseEntity.ok(userService.getMe());
	}
}
