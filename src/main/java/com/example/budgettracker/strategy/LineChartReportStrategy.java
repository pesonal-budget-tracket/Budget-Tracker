package com.example.budgettracker.strategy;

import com.example.budgettracker.model.Transaction;
import com.example.budgettracker.service.TransactionService;
import org.springframework.ui.Model;

import java.time.*;
import java.util.*;

public class LineChartReportStrategy implements ReportStrategy {

    private final TransactionService transactionService;

    public LineChartReportStrategy(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public String generate(String username, Model model) {
        List<Transaction> transactions = transactionService.getTransactionsForUser(username);

        Map<String, Double> incomeByMonth = new LinkedHashMap<>();
        Map<String, Double> expenseByMonth = new LinkedHashMap<>();

        for (int i = 1; i <= 12; i++) {
            String monthName = Month.of(i).name().substring(0, 3);
            incomeByMonth.put(monthName, 0.0);
            expenseByMonth.put(monthName, 0.0);
        }

        for (Transaction t : transactions) {
            LocalDate date = t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String month = date.getMonth().name().substring(0, 3);

            if (t.getClass().getSimpleName().equals("IncomeTransaction")) {
                incomeByMonth.put(month, incomeByMonth.get(month) + t.getAmount());
            } else if (t.getClass().getSimpleName().equals("ExpenseTransaction")) {
                expenseByMonth.put(month, expenseByMonth.get(month) + t.getAmount());
            }
        }

        model.addAttribute("incomeData", incomeByMonth.values());
        model.addAttribute("expenseData", expenseByMonth.values());
        model.addAttribute("labels", incomeByMonth.keySet());
        model.addAttribute("username", username);

        return "report-line-chart";
    }
}
