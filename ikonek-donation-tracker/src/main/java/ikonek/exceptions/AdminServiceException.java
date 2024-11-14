package ikonek.exceptions;

public class AdminServiceException extends RuntimeException {
    public AdminServiceException(String message) {
        super(message);
    }
    public AdminServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}