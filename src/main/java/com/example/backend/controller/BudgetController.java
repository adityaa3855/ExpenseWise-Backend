package com.example.backend.controller;

import com.example.backend.dto.BudgetRequest;
import com.example.backend.dto.BudgetResponse;
import com.example.backend.service.BudgetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public BudgetResponse addBudget(
            @RequestBody BudgetRequest request,
            @RequestHeader("Authorization") String token
    ) {

        return budgetService.addBudget(request, token);
    }

    @GetMapping
    public List<BudgetResponse> getAllBudgets(
            @RequestHeader("Authorization") String token
    ) {

        return budgetService.getAllBudgets(token);
    }

    @PutMapping("/{id}")
    public BudgetResponse updateBudget(
            @PathVariable Long id,
            @RequestBody BudgetRequest request,
            @RequestHeader("Authorization") String token
    ) {

        return budgetService.updateBudget(id, request, token);
    }

    @DeleteMapping("/{id}")
    public String deleteBudget(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {

        budgetService.deleteBudget(id, token);

        return "Budget deleted successfully";
    }
}