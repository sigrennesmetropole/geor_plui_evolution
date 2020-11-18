/**
 * 
 */
package org.georchestra.pluievolution.service.exception;

/**
 * @author FNI18300
 *
 */
public class RestException extends Exception {

	private static final long serialVersionUID = 2561353827922106484L;

	/**
	 * 
	 */
	public RestException() {
	}

	/**
	 * @param message
	 */
	public RestException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RestException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RestException(String message, Throwable cause) {
		super(message, cause);
	}

}
