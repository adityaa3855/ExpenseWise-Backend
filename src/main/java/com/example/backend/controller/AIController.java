package com.example.backend.controller;

import com.example.backend.dto.AIRequest;
import com.example.backend.dto.AIResponse;
import com.example.backend.service.AIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/advisor")
    public AIResponse advisor(
            @RequestBody AIRequest request,
            @RequestHeader("Authorization") String token
    ) {

        return aiService.getFinancialAdvice(
                request.getPrompt(),
                token
        );

    }

}