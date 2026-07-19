package com.example.backend.dto;

public class DashboardResponse {

    private Double totalIncome;
    private Double totalExpense;
    private Double balance;

    public DashboardResponse() {
    }

    public DashboardResponse(Double totalIncome,
                             Double totalExpense,
                             Double balance) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = balance;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}