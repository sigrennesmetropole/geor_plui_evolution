/**
 * 
 */
package org.georchestra.pluievolution.service.sm.impl;

import org.georchestra.pluievolution.service.exception.InitializationException;
import org.georchestra.pluievolution.service.sm.InitializationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author FNI18300
 *
 */
@Service
public class InitializationServiceImpl implements InitializationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationServiceImpl.class);

	@Override
	public void initialize() throws InitializationException {
		LOGGER.info("Start initialization...");
	}

}
