package com.example.budgettracker.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.budgettracker.factory.TransactionFactory;
import com.example.budgettracker.model.Transaction;
import com.example.budgettracker.singleton.BudgetManager;

@Service
public class TransactionService {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final BudgetManager budgetManager;

    public TransactionService(BudgetManager budgetManager) {
        this.budgetManager = budgetManager;
    }

    public void addTransaction(String username, String type, double amount, Date date, String description) {
        Transaction transaction = TransactionFactory.createTransaction(type, amount, date, description);
        saveTransactionToCSV(username, transaction);

        if (type.equalsIgnoreCase("income")) {
            budgetManager.addToBudget(username, amount);
        } else if (type.equalsIgnoreCase("expense")) {
            budgetManager.deductFromBudget(username, amount);
        }
    }

    public List<Transaction> getTransactionsForUser(String username) {
        String csvFile = "data/" + username + "_transactions.csv";
        List<Transaction> transactions = new ArrayList<>();

        Path path = Paths.get(csvFile);
        if (!Files.exists(path)) return transactions;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 4) continue;

                String type = data[0];
                double amount = Double.parseDouble(data[1]);
                Date date = DATE_FORMAT.parse(data[3]);
                String description = data[2];

                Transaction transaction = TransactionFactory.createTransaction(type.toLowerCase(), amount, date, description);
                transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public double calculateTotalBudget(String username) {
        return budgetManager.getUserBudget(username);
    }

    private void saveTransactionToCSV(String username, Transaction transaction) {
        String csvFile = "data/" + username + "_transactions.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            String formattedDate = DATE_FORMAT.format(transaction.getDate());
            writer.write(transaction.getClass().getSimpleName() + "," + transaction.getAmount() + "," +
                         transaction.getDescription() + "," + formattedDate);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}