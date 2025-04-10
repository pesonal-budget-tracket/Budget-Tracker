package com.example.budgettracker.observer;

import java.util.ArrayList;
import java.util.List;

public class BudgetAlertNotifier {
    private final List<BudgetObserver> observers = new ArrayList<>();

    public void register(BudgetObserver observer) {
        observers.add(observer);
    }

    public void notify(String username, String category, double limit, double actual) {
        for (BudgetObserver observer : observers) {
            observer.onBudgetExceeded(username, category, limit, actual);
        }
    }
}
