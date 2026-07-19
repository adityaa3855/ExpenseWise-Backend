package com.example.backend.dto;

import java.time.LocalDate;

public class RecentTransactionResponse {

    private Long id;
    private String title;
    private Double amount;
    private String category;
    private String type;
    private LocalDate date;

    public RecentTransactionResponse() {
    }

    public RecentTransactionResponse(Long id,
                                     String title,
                                     Double amount,
                                     String category,
                                     String type,
                                     LocalDate date) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}