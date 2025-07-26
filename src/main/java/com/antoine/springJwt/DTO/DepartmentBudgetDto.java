package com.antoine.springJwt.dto;

public class DepartmentBudgetDto {
    private String department;
    private double allocated;
    private double spent;
    private double remaining;

    // constructors 
    public DepartmentBudgetDto(String department, double allocated, double spent, double remaining) {
        this.department = department;
        this.allocated = allocated;
        this.spent = spent;
        this.remaining = remaining;
    }

    // Getters and Setters

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getAllocated() {
        return allocated;
    }

    public void setAllocated(double allocated) {
        this.allocated = allocated;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }
    

    
}
