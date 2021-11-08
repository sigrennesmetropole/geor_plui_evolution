package org.georchestra.pluievolution.service.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Codes "custom" des status remontés en front en cas d'erreur de saisie de l'utilsateur.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiServiceExceptionsStatus {
	
	public static final String BAD_REQUEST  = String.valueOf(HttpStatus.BAD_REQUEST.value());
	
	public static final String NOT_FOUND  = String.valueOf(HttpStatus.NOT_FOUND.value());

}
