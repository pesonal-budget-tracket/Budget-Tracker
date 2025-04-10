package com.example.budgettracker.observer;

public interface BudgetObserver {
    void onBudgetExceeded(String username, String category, double limit, double actual);
}
