/**
 * 
 */
package org.georchestra.pluievolution.service.exception;

/**
 * @author FNI18300
 *
 */
public class EMailException extends Exception {

	private static final long serialVersionUID = 533514335237154897L;

	/**
	 * 
	 */
	public EMailException() {
	}

	/**
	 * @param message
	 */
	public EMailException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public EMailException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EMailException(String message, Throwable cause) {
		super(message, cause);
	}

}
