package com.example.budgettracker.service;

import com.example.budgettracker.model.CategoryBudget;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryBudgetService {

    private static final String FILE_PATH = "data/category_budgets.csv";

    public void saveCategoryBudget(CategoryBudget cb) {
        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            writer.write(cb.getUsername() + "," +
                         cb.getCategory() + "," +
                         cb.getMonth() + "," +
                         cb.getYear() + "," +
                         cb.getBudgetAmount() + "\n");
        } catch (IOException e) {
            System.err.println("Error writing category budget: " + e.getMessage());
        }
    }

    public CategoryBudget getCategoryBudget(String username, String category, int month, int year) {
        try (Scanner scanner = new Scanner(new File(FILE_PATH))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");

                if (data.length == 5 &&
                    data[0].equals(username) &&
                    data[1].equalsIgnoreCase(category) &&
                    Integer.parseInt(data[2]) == month &&
                    Integer.parseInt(data[3]) == year) {

                    double budgetAmount = Double.parseDouble(data[4]);
                    return new CategoryBudget(username, category, month, year, budgetAmount);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading category budget: " + e.getMessage());
        }

        return null;
    }

public List<CategoryBudget> getBudgetsForUserAndMonth(String username, int month, int year) {
    List<CategoryBudget> budgets = new ArrayList<>();
    String filePath = "data/category_budgets.csv";

    try (Scanner scanner = new Scanner(new File(filePath))) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] data = line.split(",");

            if (data.length == 5 &&
                data[0].equals(username) &&
                Integer.parseInt(data[2]) == month &&
                Integer.parseInt(data[3]) == year) {

                String category = data[1];
                double budgetAmount = Double.parseDouble(data[4]);
                CategoryBudget cb = new CategoryBudget(username, category, month, year, budgetAmount);
                budgets.add(cb);
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading category budgets: " + e.getMessage());
    }

    return budgets;
}
}
