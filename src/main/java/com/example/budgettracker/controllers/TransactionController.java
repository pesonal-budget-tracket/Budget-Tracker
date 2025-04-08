package com.example.budgettracker.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.budgettracker.model.Transaction;
import com.example.budgettracker.service.TransactionService;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public String getTransactions(@RequestParam String username, Model model) {
        List<Transaction> transactions = transactionService.getTransactionsForUser(username);
        double totalBudget = transactionService.calculateTotalBudget(username);

        model.addAttribute("transactions", transactions);
        model.addAttribute("username", username);
        model.addAttribute("totalBudget", totalBudget);
        return "transaction"; // 👈 match the HTML file name: transaction.html
    }

    @PostMapping("/addTransaction")
    public String addTransaction(
            @RequestParam String username,
            @RequestParam String type,
            @RequestParam double amount,
            @RequestParam String description) {

        transactionService.addTransaction(username, type, amount, new Date(), description);
        return "redirect:/transactions?username=" + username;
    }
}
