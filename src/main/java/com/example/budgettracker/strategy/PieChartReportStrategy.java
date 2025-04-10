package com.example.budgettracker.strategy;

import com.example.budgettracker.model.Transaction;
import com.example.budgettracker.service.TransactionService;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartReportStrategy implements ReportStrategy {

    private final TransactionService transactionService;

    public PieChartReportStrategy(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public String generate(String username, Model model) {
        List<Transaction> transactions = transactionService.getTransactionsForUser(username);

        // Filter for current month
        Map<String, Double> categoryTotals = new HashMap<>();
        LocalDate now = LocalDate.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        for (Transaction t : transactions) {
            LocalDate transDate = t.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            if (transDate.getMonthValue() == currentMonth && transDate.getYear() == currentYear) {
                if (t instanceof com.example.budgettracker.model.ExpenseTransaction) {
                    String cat = t.getCategory();
                    categoryTotals.put(cat, categoryTotals.getOrDefault(cat, 0.0) + t.getAmount());
                }
            }
        }

        model.addAttribute("categoryData", categoryTotals);
        model.addAttribute("username", username);
        return "report-pie-chart";
    }
}
