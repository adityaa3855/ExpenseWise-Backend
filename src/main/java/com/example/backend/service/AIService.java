package com.example.backend.service;

import com.example.backend.dto.AIResponse;
import com.example.backend.entity.Expense;
import com.example.backend.entity.User;
import com.example.backend.repository.ExpenseRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AIService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final RestClient restClient;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AIService(
            RestClient restClient,
            ExpenseRepository expenseRepository,
            UserRepository userRepository,
            JwtUtil jwtUtil
    ) {
        this.restClient = restClient;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public AIResponse getFinancialAdvice(String prompt, String token) {

        String email = jwtUtil.extractEmail(token.substring(7));

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Expense> expenses = expenseRepository.findByUser(user);

        double totalIncome = 0;
        double totalExpense = 0;

        Map<String, Double> categoryWise = new HashMap<>();

        for (Expense expense : expenses) {

            if ("INCOME".equalsIgnoreCase(expense.getType())) {

                totalIncome += expense.getAmount();

            } else {

                totalExpense += expense.getAmount();

                categoryWise.put(
                        expense.getCategory(),
                        categoryWise.getOrDefault(expense.getCategory(), 0.0)
                                + expense.getAmount()
                );
            }
        }

        double balance = totalIncome - totalExpense;

        List<Map.Entry<String, Double>> sortedCategories =
                categoryWise.entrySet()
                        .stream()
                        .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                        .collect(Collectors.toList());

        StringBuilder summary = new StringBuilder();

        summary.append("Financial Summary\n");
        summary.append("----------------------------\n");
        summary.append("Total Income : ₹").append(totalIncome).append("\n");
        summary.append("Total Expenses : ₹").append(totalExpense).append("\n");
        summary.append("Current Balance : ₹").append(balance).append("\n\n");

        summary.append("Expense Categories (Highest First)\n");

        if (sortedCategories.isEmpty()) {

            summary.append("No expenses available.\n");

        } else {

            for (Map.Entry<String, Double> entry : sortedCategories) {

                summary.append(entry.getKey())
                        .append(" : ₹")
                        .append(entry.getValue())
                        .append("\n");
            }
        }

        String finalPrompt = """
You are ExpenseWise AI, a professional financial advisor inside an AI Expense Tracker.

Understand the user's intent first.

==========================
SPECIFIC QUESTION
==========================

Examples:
- How much did I spend on Food?
- What is my current balance?
- Which category has the highest expense?
- Did I spend more on Food than Shopping?
- How many transactions do I have?

Rules:
- Answer ONLY the user's question.
- Maximum 5 short lines.
- Be direct and precise.
- Do NOT generate a financial report.
- Do NOT provide savings tips or warnings unless the user asks.

==========================
FINANCIAL ANALYSIS / ADVICE
==========================

Examples:
- Analyze my expenses.
- Give me financial advice.
- Review my spending.
- Give me a financial report.
- How is my financial health?
- Help me save money.

Generate EXACTLY these sections:

📊 Financial Summary

📈 Overspending Analysis

⚠️ Warnings

💡 Savings Tips

🎯 Final Advice

Rules:
- Each section should contain only 2-3 short sentences.
- Keep the entire response under 250 words.
- Base every recommendation ONLY on the financial summary.
- If a section has nothing to report, write "None".

==========================
GENERAL RULES
==========================

- Use ONLY the financial summary below.
- Never invent expenses, categories, amounts or transactions.
- Use the exact category names from the summary.
- If there is insufficient data, clearly say so.
- Use simple professional English.
- Never apologize.
- Never repeat the same information.
- Never mix the two response formats.

User Question:
%s

Financial Summary:
%s
""".formatted(prompt, summary.toString());
        String url = "https://api.groq.com/openai/v1/chat/completions";

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", finalPrompt);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.3-70b-versatile");
        body.put("messages", List.of(message));
        body.put("temperature", 0.4);

        try {

            Map response = restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + apiKey)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            List choices = (List) response.get("choices");

            Map choice = (Map) choices.get(0);

            Map messageResponse = (Map) choice.get("message");

            String answer = messageResponse.get("content").toString();

            return new AIResponse(answer);

        } catch (Exception e) {

            e.printStackTrace();

            return new AIResponse("Error: " + e.getMessage());

        }

    }

}