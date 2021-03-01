/**
 * 
 */
package org.georchestra.pluievolution.service.helper.authentification;

import java.util.List;
import java.util.stream.Collectors;

import org.georchestra.pluievolution.core.dto.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author FNI18300
 *
 */
@Component
public class AuthentificationHelper {

	public static final String ADMINISTRATOR_ROLE = "ADMINISTRATOR";

	/**
	 * @return Retourne l'utilisateur connecté
	 */
	public User getConnectedUser() {
		User user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() != null) {
			user = (User) authentication.getDetails();
		}
		return user;
	}

	/**
	 * 
	 * @return l'username de la personne authentifiée
	 */
	public String getUsername() {
		String username = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() != null) {
			username = authentication.getPrincipal().toString();
		}
		return username;
	}

	/**
	 * 
	 * @return vrai si l'utilisateur connecté est administrateur
	 */
	public boolean isAdmin() {
		return hasRole(ADMINISTRATOR_ROLE);
	}

	/**
	 * @return la liste des rôles
	 */
	public List<String> getRoles() {
		List<String> result = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			result = authentication.getAuthorities().stream().map(authority -> authority.getAuthority())
					.collect(Collectors.toList());
		}
		return result;
	}

	/**
	 * 
	 * @param roleName
	 * @return
	 */
	public boolean hasRole(String roleName) {
		boolean result = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			result = authentication.getAuthorities().stream()
					.filter(authority -> authority.getAuthority().equalsIgnoreCase(roleName)).count() > 0;
		}
		return result;
	}
	public String getOrganisation() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User details  = (User) authentication.getDetails();
		return details.getOrganization();
	}
}
