package ikonek.exceptions;

public class BloodDonationServiceException extends Exception {
    public BloodDonationServiceException(String message) {
        super(message);
    }

    public BloodDonationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
