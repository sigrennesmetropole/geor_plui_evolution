package org.georchestra.pluievolution.service.exception;

public class DocumentRepositoryException extends Exception {

	private static final long serialVersionUID = 1L;

	public DocumentRepositoryException() {
		super();
	}

	public DocumentRepositoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentRepositoryException(String message) {
		super(message);
	}

	public DocumentRepositoryException(Throwable cause) {
		super(cause);
	}

}
