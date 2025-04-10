package com.example.budgettracker.strategy;

import org.springframework.ui.Model;

public interface ReportStrategy {
    String generate(String username, Model model);
}
