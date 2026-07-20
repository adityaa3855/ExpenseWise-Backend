package com.example.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to AI Expense Tracker Backend";
    }


    @GetMapping("/profile")
    public String profile() {

        return "Welcome Authenticated User";

    }

}

