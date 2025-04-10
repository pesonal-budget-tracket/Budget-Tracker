package com.example.budgettracker.model;

import java.util.Date;

public class IncomeTransaction extends Transaction {
    public IncomeTransaction(double amount, Date date, String description, String category) {
        super(amount, date, description, category);
    }

    @Override
    public String getDetails() {
        return "[Income] Amount: " + amount +
               ", Date: " + date +
               ", Description: " + description +
               ", Category: " + category;
    }
}
