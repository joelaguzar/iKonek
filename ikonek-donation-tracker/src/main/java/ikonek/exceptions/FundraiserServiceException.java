package ikonek.exceptions;

public class FundraiserServiceException extends Exception {
    public FundraiserServiceException(String message) {
        super(message);
    }

    public FundraiserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}