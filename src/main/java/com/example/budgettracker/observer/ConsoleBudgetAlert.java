package com.example.budgettracker.observer;

public class ConsoleBudgetAlert implements BudgetObserver {
    @Override
    public void onBudgetExceeded(String username, String category, double limit, double actual) {
        System.out.println("⚠️ ALERT: [" + username + "] exceeded budget in " + category +
                           " | Limit: ₹" + limit + " | Actual: ₹" + actual);
    }
}
