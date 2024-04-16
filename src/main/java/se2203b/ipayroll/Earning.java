package se2203b.ipayroll;

import java.sql.Date;

public class Earning {
    private int earningID;

    private String earningSourceName;
    private Double amount, ratePerHour;
    private String employeeID;

    private Date startDate, endDate;

    // Constructor with date fields
    public Earning(int earningID, Double amount, Double ratePerHour, String earningSourceName, String employeeID,
                   Date startDate, Date endDate) {
        this.earningID = earningID;
        this.amount = amount;
        this.ratePerHour = ratePerHour;
        this.earningSourceName = earningSourceName;
        this.employeeID = employeeID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Earning() {}

    // Setters for date fields
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // Getters for date fields
    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    // Setters for other fields
    public void setEarningID(int earningID) {
        this.earningID = earningID;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setRatePerHour(Double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public void setEarningSourceName(String earningSourceName) {
        this.earningSourceName = earningSourceName;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    // Getters for other fields
    public int getEarningID() {
        return earningID;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getRatePerHour() {
        return ratePerHour;
    }

    public String getEarningSourceName() {
        return earningSourceName;
    }

    public String getEmployeeID() {
        return employeeID;
    }
}
