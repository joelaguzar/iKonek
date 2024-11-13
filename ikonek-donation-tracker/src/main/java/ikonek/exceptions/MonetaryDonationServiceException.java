package ikonek.exceptions;

public class MonetaryDonationServiceException extends Exception {
    public MonetaryDonationServiceException(String message) {
        super(message);
    }

    public MonetaryDonationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}