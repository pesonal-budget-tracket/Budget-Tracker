package com.example.budgettracker.service;

import com.example.budgettracker.model.ExpectedBudget;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;

@Service
public class ExpectedBudgetService {

    private static final String FILE_PATH = "data/expected_budgets.csv";

    public void saveExpectedBudget(ExpectedBudget budget) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(budget.getUsername() + "," +
                         budget.getMonth() + "," +
                         budget.getYear() + "," +
                         budget.getExpectedIncome() + "," +
                         budget.getExpectedExpense() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing expected budget: " + e.getMessage());
        }
    }

    public ExpectedBudget getExpectedBudget(String username, int month, int year) {
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                if (data.length == 5 &&
                    data[0].equals(username) &&
                    Integer.parseInt(data[1]) == month &&
                    Integer.parseInt(data[2]) == year) {

                    double expectedIncome = Double.parseDouble(data[3]);
                    double expectedExpense = Double.parseDouble(data[4]);

                    return new ExpectedBudget(username, month, year, expectedIncome, expectedExpense);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading expected budget: " + e.getMessage());
        }

        return null;
    }
    public ExpectedBudget getBudgetForUserAndMonth(String username, int month, int year) {
        String filePath = "data/expected_budgets.csv";
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5 &&
                    data[0].equals(username) &&
                    Integer.parseInt(data[1]) == month &&
                    Integer.parseInt(data[2]) == year) {
    
                    double expectedIncome = Double.parseDouble(data[3]);
                    double expectedExpense = Double.parseDouble(data[4]);
    
                    return new ExpectedBudget(username, month, year, expectedIncome, expectedExpense);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null;
    }
    
}
