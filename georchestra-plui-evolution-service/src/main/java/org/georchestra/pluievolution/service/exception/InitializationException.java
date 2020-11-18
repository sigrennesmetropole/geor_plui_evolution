/**
 * 
 */
package org.georchestra.pluievolution.service.exception;

/**
 * @author FNI18300
 *
 */
public class InitializationException extends Exception {

	private static final long serialVersionUID = -6313510009433868085L;

	/**
	 * 
	 */
	public InitializationException() {
	}

	/**
	 * @param message
	 */
	public InitializationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InitializationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InitializationException(String message, Throwable cause) {
		super(message, cause);
	}

}
