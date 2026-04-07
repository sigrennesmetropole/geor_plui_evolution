package org.georchestra.pluievolution.service.exception;

public class ApiServiceException extends Exception {

    private static final long serialVersionUID = 1L;
    
    private final String appExceptionStatusCode;

    public ApiServiceException() {
        super();
        this.appExceptionStatusCode = null;
    }

    public ApiServiceException(final String message) {
        super(message);
        this.appExceptionStatusCode = null;
    }

    public ApiServiceException(final String message, final String exceptionStatusCode) {
        super(message);
        this.appExceptionStatusCode = exceptionStatusCode;
    }

    public ApiServiceException(final String message, final Throwable exception) {
        super(message, exception);
        this.appExceptionStatusCode = null;
    }

    public ApiServiceException(final String message, final Throwable exception, final String exceptionStatusCode) {
        super(message, exception);
        this.appExceptionStatusCode = exceptionStatusCode;
    }


    public String getAppExceptionStatusCode() {
        return appExceptionStatusCode;
    }

}
