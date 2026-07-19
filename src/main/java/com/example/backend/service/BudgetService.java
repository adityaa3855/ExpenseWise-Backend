package com.example.backend.service;

import com.example.backend.dto.BudgetRequest;
import com.example.backend.dto.BudgetResponse;
import com.example.backend.entity.Budget;
import com.example.backend.entity.User;
import com.example.backend.repository.BudgetRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public BudgetService(
            BudgetRepository budgetRepository,
            UserRepository userRepository,
            JwtUtil jwtUtil
    ) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    private User getUser(String token) {

        String email = jwtUtil.extractEmail(token.substring(7));

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public BudgetResponse addBudget(BudgetRequest request, String token) {

        User user = getUser(token);

        Optional<Budget> existing = budgetRepository
                .findByUserAndCategoryAndMonthAndYear(
                        user,
                        request.getCategory(),
                        request.getMonth(),
                        request.getYear()
                );

        Budget budget;

        if (existing.isPresent()) {

            budget = existing.get();

        } else {

            budget = new Budget();
            budget.setUser(user);
            budget.setCategory(request.getCategory());
            budget.setMonth(request.getMonth());
            budget.setYear(request.getYear());
        }

        budget.setAmount(request.getAmount());

        Budget saved = budgetRepository.save(budget);

        return new BudgetResponse(
                saved.getId(),
                saved.getCategory(),
                saved.getAmount(),
                saved.getMonth(),
                saved.getYear()
        );
    }

    public List<BudgetResponse> getAllBudgets(String token) {

        User user = getUser(token);

        List<Budget> budgets = budgetRepository.findByUser(user);

        List<BudgetResponse> response = new ArrayList<>();

        for (Budget budget : budgets) {

            response.add(new BudgetResponse(
                    budget.getId(),
                    budget.getCategory(),
                    budget.getAmount(),
                    budget.getMonth(),
                    budget.getYear()
            ));
        }

        return response;
    }

    public BudgetResponse updateBudget(Long id,
                                       BudgetRequest request,
                                       String token) {

        User user = getUser(token);

        Budget budget = budgetRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        budget.setCategory(request.getCategory());
        budget.setAmount(request.getAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        Budget updated = budgetRepository.save(budget);

        return new BudgetResponse(
                updated.getId(),
                updated.getCategory(),
                updated.getAmount(),
                updated.getMonth(),
                updated.getYear()
        );
    }

    public void deleteBudget(Long id, String token) {

        User user = getUser(token);

        Budget budget = budgetRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        budgetRepository.delete(budget);
    }
}