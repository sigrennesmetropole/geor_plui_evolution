/**
 * 
 */
package org.georchestra.pluievolution.service.sm;

import org.georchestra.pluievolution.core.dto.User;


/**
 * @author FNI18300
 *
 */
public interface UserService {

	/**
	 * Retourne l'utilisateur courant stocké dans le security context
	 *
	 * @return l'utilisateur courant avec ses rôles
	 */
	User getMe();

	/**
	 * Retoure un utilisateur par son login.
	 * 
	 * @param login
	 * @return
	 */
	User loadUserByUsername(String login);

	/**
	 * Créé un utilisateur
	 * 
	 * @param user
	 * @return l'utilisateur créé
	 */
	User createUser(User user);

	/**
	 * Update un utilisateur
	 * 
	 * @param user
	 * @return l'utilisateur mis à jour
	 */
	User updateUser(User user);
}
