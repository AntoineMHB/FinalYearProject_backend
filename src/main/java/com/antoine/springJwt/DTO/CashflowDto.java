package com.antoine.springJwt.dto;



public class CashflowDto {
    private Double totalRevenue;
    private Double totalExpenses;
    private Double netProfit;

    public CashflowDto(Double totalRevenue, Double totalExpenses, Double netProfit) {
        this.totalRevenue = totalRevenue;
        this.totalExpenses = totalExpenses;
        this.netProfit = netProfit;
    }

    // Getters and setters

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(Double netProfit) {
        this.netProfit = netProfit;
    }


}
