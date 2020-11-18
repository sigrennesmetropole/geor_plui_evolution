/**
 * 
 */
package org.georchestra.pluievolution.api.controller;

import org.georchestra.pluievolution.api.UserApi;
import org.georchestra.pluievolution.core.dto.User;
import org.georchestra.pluievolution.service.st.ldap.LdapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * @author FNI18300
 *
 */
@RestController
@Api(tags = "user")
public class UserController implements UserApi {

	@Autowired
	private LdapService userService;

	@Override
	public ResponseEntity<User> getMe() throws Exception {
		return ResponseEntity.ok(userService.getMe());
	}
}
