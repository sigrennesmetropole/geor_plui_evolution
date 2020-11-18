/**
 * 
 */
package org.georchestra.pluievolution.service.sm;

import org.georchestra.pluievolution.service.exception.InitializationException;

/**
 * @author FNI18300
 *
 */
public interface InitializationService {

	/**
	 * Initialise les éléments qui ne peuvent pas être intialisés en base
	 * directement
	 * 
	 * @throws InitializationException
	 */
	void initialize() throws InitializationException;

}
