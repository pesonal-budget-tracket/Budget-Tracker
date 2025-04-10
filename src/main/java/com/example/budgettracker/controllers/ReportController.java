package com.example.budgettracker.controllers;

import com.example.budgettracker.service.TransactionService;
import com.example.budgettracker.strategy.BarChartReportStrategy;
import com.example.budgettracker.strategy.PieChartReportStrategy;
import com.example.budgettracker.strategy.LineChartReportStrategy;
import com.example.budgettracker.strategy.ReportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReportController {

    private final TransactionService transactionService;

    @Autowired
    public ReportController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/report")
    public String generateReport(@RequestParam String username,
                                 @RequestParam(defaultValue = "bar") String type,
                                 Model model) {

        ReportStrategy strategy;

        switch (type.toLowerCase()) {
            case "pie":
                strategy = new PieChartReportStrategy(transactionService);
                break;
            case "line":
                strategy = new LineChartReportStrategy(transactionService);
                break;
            case "bar":
            default:
                strategy = new BarChartReportStrategy(transactionService);
                break;
        }

        return strategy.generate(username, model);
    }

    @GetMapping("/report/dashboard")
    public String showReportDashboard(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        return "report-dashboard";
    }
}
