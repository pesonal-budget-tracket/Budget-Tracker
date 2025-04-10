package com.example.budgettracker.model;

import java.util.Date;

public class ExpenseTransaction extends Transaction {
    public ExpenseTransaction(double amount, Date date, String description, String category) {
        super(amount, date, description, category);
    }

    @Override
    public String getDetails() {
        return "[Expense] Amount: " + amount +
               ", Date: " + date +
               ", Description: " + description +
               ", Category: " + category;
    }
}
