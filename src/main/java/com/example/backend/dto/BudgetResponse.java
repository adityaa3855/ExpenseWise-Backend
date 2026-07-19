package com.example.backend.dto;

public class BudgetResponse {

    private Long id;
    private String category;
    private Double amount;
    private Integer month;
    private Integer year;

    public BudgetResponse() {
    }

    public BudgetResponse(Long id, String category, Double amount, Integer month, Integer year) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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