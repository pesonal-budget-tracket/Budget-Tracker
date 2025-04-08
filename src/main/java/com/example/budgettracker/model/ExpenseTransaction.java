package com.example.budgettracker.model;
import java.util.Date; // Import necessary class

public class ExpenseTransaction extends Transaction {
    public ExpenseTransaction(double amount, Date date, String description) {
        super(amount, date, description);
    }

    @Override
    public String getDetails() {
        return "[Expense] Amount: " + amount + ", Date: " + date + ", Description: " + description;
    }
}
