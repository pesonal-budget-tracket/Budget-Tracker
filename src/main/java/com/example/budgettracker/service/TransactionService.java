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
import java.time.LocalDate;
import java.time.ZoneId;


import org.springframework.stereotype.Service;
import com.example.budgettracker.model.ExpenseTransaction;
import com.example.budgettracker.model.IncomeTransaction;


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

    public void addTransaction(String username, String type, double amount, Date date, String description, String category)
    {
        Transaction transaction = TransactionFactory.createTransaction(type, amount, date, description, category);

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
                String rawType = data[0].toLowerCase(); // e.g., "incometransaction"

                String type;
                if (rawType.contains("income")) {
                    type = "income";
                } else if (rawType.contains("expense")) {
                    type = "expense";
                } else {
                    continue; // skip if unknown type
                }
                
                double amount = Double.parseDouble(data[1]);
                String description = data[2];
                String category = data[3];
                Date date = DATE_FORMAT.parse(data[4]);
                
                Transaction transaction = TransactionFactory.createTransaction(type, amount, date, description, category);
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
             transaction.getDescription() + "," + transaction.getCategory() + "," + formattedDate);

            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public double getTotalSpentForCategory(String username, String category, int month, int year) {
        List<Transaction> transactions = getTransactionsForUser(username);
        double total = 0.0;
    
        for (Transaction t : transactions) {
            if (t instanceof com.example.budgettracker.model.ExpenseTransaction) {
                String transCategory = t.getCategory();
                if (transCategory != null && transCategory.equalsIgnoreCase(category)) {
                    Date date = t.getDate();
                    @SuppressWarnings("deprecation")
                    int transMonth = date.getMonth() + 1;
                    @SuppressWarnings("deprecation")
                    int transYear = date.getYear() + 1900;
    
                    if (transMonth == month && transYear == year) {
                        total += t.getAmount();
                    }
                }
            }
        }
    
        return total;
    }
    public double getTotalIncomeForMonth(String username, int month, int year) {
        return getTransactionsForUser(username).stream()
            .filter(t -> t instanceof IncomeTransaction)
            .filter(t -> {
                LocalDate localDate = t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return localDate.getMonthValue() == month && localDate.getYear() == year;
            })
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
    
    public double getTotalExpenseForMonth(String username, int month, int year) {
        return getTransactionsForUser(username).stream()
            .filter(t -> t instanceof ExpenseTransaction)
            .filter(t -> {
                LocalDate localDate = t.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return localDate.getMonthValue() == month && localDate.getYear() == year;
            })
            .mapToDouble(Transaction::getAmount)
            .sum();
    }
    
    
    
}