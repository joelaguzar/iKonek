package ikonek.exceptions;

public class HospitalServiceException extends RuntimeException {
    public HospitalServiceException(String message) {
        super(message);
    }

    public HospitalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
