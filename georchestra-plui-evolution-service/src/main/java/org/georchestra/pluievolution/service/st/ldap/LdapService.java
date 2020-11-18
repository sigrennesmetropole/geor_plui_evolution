/**
 * 
 */
package org.georchestra.pluievolution.service.st.ldap;

import org.georchestra.pluievolution.core.dto.User;

/**
 * @author FNI18300
 *
 */
public interface LdapService {

	/**
	 * Retourne l'utilisateur courant stocké dans le security context
	 * 
	 * @see LdapService.getUserByLogin
	 * @return l'utilisateur courant avec ses rôles
	 */
	User getMe();

	/**
	 * Retoure un utilisateur par son login. Attention les informations sont
	 * extraites du LDAP. Mais cette méthode ne peut pas extraire les rôles du User
	 * correspondant
	 * 
	 * @param login
	 * @return
	 */
	User getUserByLogin(String login);
}
