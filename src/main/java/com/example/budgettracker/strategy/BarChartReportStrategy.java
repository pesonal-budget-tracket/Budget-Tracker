package com.example.budgettracker.strategy;

import com.example.budgettracker.model.Transaction;
import com.example.budgettracker.model.ExpenseTransaction;
import com.example.budgettracker.service.TransactionService;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarChartReportStrategy implements ReportStrategy {

    private final TransactionService transactionService;

    public BarChartReportStrategy(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public String generate(String username, Model model) {
        List<Transaction> transactions = transactionService.getTransactionsForUser(username);

        Map<String, Double> categoryTotals = new HashMap<>();
        LocalDate now = LocalDate.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        for (Transaction t : transactions) {
            if (t instanceof ExpenseTransaction) {
                LocalDate date = t.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                if (date.getMonthValue() == month && date.getYear() == year) {
                    String cat = t.getCategory();
                    categoryTotals.put(cat, categoryTotals.getOrDefault(cat, 0.0) + t.getAmount());
                }
            }
        }

        model.addAttribute("categoryData", categoryTotals);
        model.addAttribute("username", username);
        return "report-bar-chart";
    }
}
