package se2203b.ipayroll;

import java.sql.Date;

public class Deduction {
    private int deductionID;
    private double amount;
    private double percentage;
    private double upperLimit;
    private Date startDate;
    private Date endDate;
    private String deductionSource;
    private String employeeID;

    // Constructor
    public Deduction(int deductionID, double amount, double percentage, double upperLimit, Date startDate, Date endDate, String deductionSource, String employeeID) {
        this.deductionID = deductionID;
        this.amount = amount;
        this.percentage = percentage;
        this.upperLimit = upperLimit;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deductionSource = deductionSource;
        this.employeeID = employeeID;
    }

    public Deduction() {}

    // Getters
    public int getDeductionID() {
        return deductionID;
    }

    public double getAmount() {
        return amount;
    }

    public double getPercentage() {
        return percentage;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getDeductionSource() {
        return deductionSource;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    // Setters
    public void setDeductionID(int deductionID) {
        this.deductionID = deductionID;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public void setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setDeductionSource(String deductionSource) {
        this.deductionSource = deductionSource;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }
}

