package com.example.backend.dto;

public class BudgetRequest {

    private String category;
    private Double amount;
    private Integer month;
    private Integer year;

    public BudgetRequest() {
    }

    public BudgetRequest(String category, Double amount, Integer month, Integer year) {
        this.category = category;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}