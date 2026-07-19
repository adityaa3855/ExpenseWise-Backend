package com.example.backend.service;

import com.example.backend.dto.ExpenseRequest;
import com.example.backend.dto.ExpenseResponse;
import com.example.backend.entity.Expense;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repository.ExpenseRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Add Expense
    public String addExpense(ExpenseRequest request, String token) {

        token = token.substring(7);

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Expense expense = new Expense();

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setType(request.getType());
        expense.setDate(request.getDate());
        expense.setDescription(request.getDescription());

        expense.setUser(user);

        expenseRepository.save(expense);

        return "Expense Added Successfully";
    }

    // Get All Expenses of Logged-in User
    public List<ExpenseResponse> getAllExpenses(String token) {

        token = token.substring(7);

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        List<Expense> expenses = expenseRepository.findByUser(user);

        List<ExpenseResponse> response = new ArrayList<>();

        for (Expense expense : expenses) {

            ExpenseResponse dto = new ExpenseResponse();

            dto.setId(expense.getId());
            dto.setTitle(expense.getTitle());
            dto.setAmount(expense.getAmount());
            dto.setCategory(expense.getCategory());
            dto.setType(expense.getType());
            dto.setDate(expense.getDate());
            dto.setDescription(expense.getDescription());

            response.add(dto);
        }

        return response;
    }

    // Get Expense By ID
    public ExpenseResponse getExpenseById(Long id, String token) {

        token = token.substring(7);

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense Not Found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access Denied");
        }

        ExpenseResponse response = new ExpenseResponse();

        response.setId(expense.getId());
        response.setTitle(expense.getTitle());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setType(expense.getType());
        response.setDate(expense.getDate());
        response.setDescription(expense.getDescription());

        return response;
    }

    // Update Expense
    public String updateExpense(Long id, ExpenseRequest request, String token) {

        token = token.substring(7);

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense Not Found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access Denied");
        }

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setType(request.getType());
        expense.setDate(request.getDate());
        expense.setDescription(request.getDescription());

        expenseRepository.save(expense);

        return "Expense Updated Successfully";
    }

    // Delete Expense
    public String deleteExpense(Long id, String token) {

        token = token.substring(7);

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense Not Found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access Denied");
        }

        expenseRepository.delete(expense);

        return "Expense Deleted Successfully";
    }
}