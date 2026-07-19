package com.example.backend.service;

import com.example.backend.dto.DashboardResponse;
import com.example.backend.entity.Expense;
import com.example.backend.entity.User;
import com.example.backend.repository.ExpenseRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import com.example.backend.dto.RecentTransactionResponse;
import org.springframework.data.domain.PageRequest;
import java.util.ArrayList;
import java.util.List;
@Service
public class DashboardService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public DashboardResponse getDashboardSummary(String token) {

        token = token.substring(7);

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        List<Expense> expenses = expenseRepository.findByUser(user);

        double totalIncome = 0;
        double totalExpense = 0;

        for (Expense expense : expenses) {

            if (expense.getType().equalsIgnoreCase("INCOME")) {
                totalIncome += expense.getAmount();
            } else {
                totalExpense += expense.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;

        return new DashboardResponse(
                totalIncome,
                totalExpense,
                balance
        );
    }

    public List<RecentTransactionResponse> getRecentTransactions(String token) {

        token = token.substring(7);

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        List<Expense> expenses = expenseRepository.findByUserOrderByDateDesc(
                user,
                PageRequest.of(0, 5)
        );

        List<RecentTransactionResponse> response = new ArrayList<>();

        for (Expense expense : expenses) {

            response.add(new RecentTransactionResponse(
                    expense.getId(),
                    expense.getTitle(),
                    expense.getAmount(),
                    expense.getCategory(),
                    expense.getType(),   // If type is an enum, we'll change this to expense.getType().name()
                    expense.getDate()
            ));
        }

        return response;
    }

}

