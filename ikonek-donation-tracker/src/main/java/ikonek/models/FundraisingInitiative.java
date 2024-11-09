package ikonek.models;

import java.time.LocalDate;

public class FundraisingInitiative {
    private int initiativeId;
    private int userId;
    private String cause;
    private double targetAmount;       // Using double
    private double amountReceived;    // Using double
    private String shortDescription;
    private LocalDate deadline;

    // Constructor
    public FundraisingInitiative(int userId, String cause, double targetAmount, String shortDescription, LocalDate deadline) {
        this.userId = userId;
        this.cause = cause;
        this.targetAmount = targetAmount;
        this.shortDescription = shortDescription;
        this.deadline = deadline;
        this.amountReceived = 0.0;  // initialize amountReceived to 0.0
    }

    // Getters and Setters
    public int getInitiativeId() {
        return initiativeId;
    }

    public void setInitiativeId(int initiativeId) {
        this.initiativeId = initiativeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "FundraisingInitiative{" +
                "initiativeId=" + initiativeId +
                ", userId=" + userId +
                ", cause='" + cause + '\'' +
                ", targetAmount=" + targetAmount +
                ", amountReceived=" + amountReceived +
                ", shortDescription='" + shortDescription + '\'' +
                ", deadline=" + deadline +
                '}';
    }
}