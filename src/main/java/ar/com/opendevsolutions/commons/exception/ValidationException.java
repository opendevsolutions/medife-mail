package ar.com.opendevsolutions.commons.exception;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 7718828512143093558L;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
