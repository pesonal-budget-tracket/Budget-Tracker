package com.example.budgettracker.model;
import java.util.Date;

public abstract class Transaction {
    protected double amount;
    protected Date date;
    protected String description;

    public Transaction(double amount, Date date, String description) {
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }
    public Date getDate() {
        return date;
    }
    public String getDescription() {
        return description;
    }
    

    public abstract String getDetails();
}
