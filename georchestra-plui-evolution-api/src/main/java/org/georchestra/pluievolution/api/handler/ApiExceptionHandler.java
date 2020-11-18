package org.georchestra.pluievolution.api.handler;

import org.georchestra.pluievolution.core.dto.ApiError;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 */
@ControllerAdvice
public class ApiExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

	/**
	 * Constructor
	 */
	public ApiExceptionHandler() {
		super();
	}

	/**
	 * Capture les exceptions de type <code>AppServiceException.class</code> pour la
	 * transformer en erreur json
	 *
	 * @param e Exception capturée
	 * @return Code erreur json
	 */
	@ExceptionHandler({ ApiServiceException.class, IllegalArgumentException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ApiError handleBadRequestException(final ApiServiceException e) {
		LOGGER.info("AppServiceException", e);
		final ApiError error = new ApiError();
		error.setCode(e.getAppExceptionStatusCode());
		error.setLabel(e.getLocalizedMessage());
		return error;
	}

}
