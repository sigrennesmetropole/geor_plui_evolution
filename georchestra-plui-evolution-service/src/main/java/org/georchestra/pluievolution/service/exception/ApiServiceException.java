package org.georchestra.pluievolution.service.exception;

public class ApiServiceException extends Exception {

    private static final long serialVersionUID = 1L;
    
    private String appExceptionStatusCode;

    public ApiServiceException() {
        super();
    }

    public ApiServiceException(final String message) {
        super(message);
    }

    public ApiServiceException(final String message, final String exceptionStatusCode) {
        super(message);
        this.appExceptionStatusCode = exceptionStatusCode;
    }

    public ApiServiceException(final String message, final Throwable exception) {
        super(message, exception);
    }

    public ApiServiceException(final String message, final Throwable exception, final String exceptionStatusCode) {
        super(message, exception);
        this.appExceptionStatusCode = exceptionStatusCode;
    }


    public String getAppExceptionStatusCode() {
        return appExceptionStatusCode;
    }

    public void setAppExceptionStatusCode(final String AppExceptionStatusCode) {
        this.appExceptionStatusCode = AppExceptionStatusCode;
    }
}
