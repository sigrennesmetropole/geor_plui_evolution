package org.georchestra.pluievolution.api.handler;

import org.georchestra.pluievolution.core.dto.ApiError;
import org.georchestra.pluievolution.service.exception.ApiServiceException;
import org.georchestra.pluievolution.service.exception.ApiServiceExceptionsStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

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

	@ExceptionHandler(ApiServiceException.class)
	protected ResponseEntity<Object> handleExceptionService(final ApiServiceException ex, final WebRequest request) {
		final ApiError apiError = createApiError(ex.getAppExceptionStatusCode() != null ? ex.getAppExceptionStatusCode()
				: HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage());

		if (ex.getAppExceptionStatusCode() != null) {
			if (ex.getAppExceptionStatusCode().equals(ApiServiceExceptionsStatus.BAD_REQUEST)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
			} else if (ex.getAppExceptionStatusCode().equals(ApiServiceExceptionsStatus.NOT_FOUND)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
			} else {
				LOGGER.error(ex.getMessage(), ex);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
			}

		} else {
			LOGGER.error(ex.getMessage(), ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
		}
	}

	/**
	 * Capture les exceptions de type <code>AppServiceException.class</code> pour la
	 * transformer en erreur json
	 *
	 * @param e Exception capturée
	 * @return Code erreur json
	 */
	@ExceptionHandler({ IllegalArgumentException.class })
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ApiError handleBadRequestException(final ApiServiceException e) {
		LOGGER.info("AppServiceException", e);
		return createApiError(e.getAppExceptionStatusCode(), e.getLocalizedMessage());
	}

	private ApiError createApiError(final String status, final String message) {
		final ApiError apiError = new ApiError();
		apiError.setCode(status);
		apiError.setLabel(message);
		return apiError;
	}

}
