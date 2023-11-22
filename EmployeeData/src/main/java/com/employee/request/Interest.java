package com.employee.request;

import javax.validation.constraints.NotBlank;

public class Interest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Principal amount is required")
    private Integer principalAmount;
    @NotBlank(message = "Rate of interest is required")
    private float rateOfInterest;
    @NotBlank(message = "Number of year is required")
    private Integer years;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(Integer principalAmount) {
        this.principalAmount = principalAmount;
    }

    public float getRateOfInterest() {
        return rateOfInterest;
    }

    public void setRateOfInterest(float rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }
}
