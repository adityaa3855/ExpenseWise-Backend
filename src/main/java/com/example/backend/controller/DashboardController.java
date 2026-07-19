package com.example.backend.controller;

import com.example.backend.dto.DashboardResponse;
import com.example.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.backend.dto.RecentTransactionResponse;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public DashboardResponse getDashboardSummary(
            @RequestHeader("Authorization") String token) {

        return dashboardService.getDashboardSummary(token);
    }

    @GetMapping("/recent")
    public List<RecentTransactionResponse> getRecentTransactions(
            @RequestHeader("Authorization") String token) {

        return dashboardService.getRecentTransactions(token);
    }

}