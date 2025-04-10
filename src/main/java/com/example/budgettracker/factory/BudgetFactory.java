package com.example.budgettracker.factory;

import com.example.budgettracker.model.ExpectedBudget;
import com.example.budgettracker.model.CategoryBudget;

public class BudgetFactory {

    public static ExpectedBudget createExpectedBudget(String username, int month, int year, double expectedIncome, double expectedExpense) {
        return new ExpectedBudget(username, month, year, expectedIncome, expectedExpense);
    }

    public static CategoryBudget createCategoryBudget(String username, String category, int month, int year, double amount) {
        return new CategoryBudget(username, category, month, year, amount);
    }
}
