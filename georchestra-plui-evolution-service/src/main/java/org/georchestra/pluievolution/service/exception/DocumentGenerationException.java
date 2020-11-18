/**
 * 
 */
package org.georchestra.pluievolution.service.exception;

/**
 * @author FNI18300
 *
 */
public class DocumentGenerationException extends Exception {

	private static final long serialVersionUID = -5187529608493534297L;

	/**
	 * 
	 */
	public DocumentGenerationException() {
	}

	/**
	 * @param message
	 */
	public DocumentGenerationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DocumentGenerationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DocumentGenerationException(String message, Throwable cause) {
		super(message, cause);
	}

}
