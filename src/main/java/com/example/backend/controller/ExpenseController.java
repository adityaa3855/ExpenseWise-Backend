package com.example.backend.controller;

import com.example.backend.dto.ExpenseRequest;

import com.example.backend.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.ExpenseResponse;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // Add Expense
    @PostMapping
    public String addExpense(
            @RequestBody ExpenseRequest request,
            @RequestHeader("Authorization") String token) {

        System.out.println("Expense Controller Called");

        return expenseService.addExpense(request, token);
    }

    // Get All Expenses of Logged-in User
    @GetMapping
    public List<ExpenseResponse> getAllExpenses(
            @RequestHeader("Authorization") String token) {

        return expenseService.getAllExpenses(token);
    }

    @GetMapping("/{id}")
    public ExpenseResponse getExpenseById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        return expenseService.getExpenseById(id, token);
    }


    @PutMapping("/{id}")
    public String updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequest request,
            @RequestHeader("Authorization") String token) {

        return expenseService.updateExpense(id, request, token);
    }

    @DeleteMapping("/{id}")
    public String deleteExpense(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {

        return expenseService.deleteExpense(id, token);
    }


}