package ikonek.models;

public interface BloodDonation extends Donation {
    int getHospitalId();
    void setHospitalId(int hospitalId);

    String getStatus();
    void setStatus(String status);

    String getFailureReason();
    void setFailureReason(String failureReason);
}