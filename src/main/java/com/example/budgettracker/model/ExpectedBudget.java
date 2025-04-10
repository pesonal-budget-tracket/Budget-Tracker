package com.example.budgettracker.model;

public class ExpectedBudget {
    private String username;
    private int month;
    private int year;
    private double expectedIncome;
    private double expectedExpense;

    public ExpectedBudget(String username, int month, int year, double expectedIncome, double expectedExpense) {
        this.username = username;
        this.month = month;
        this.year = year;
        this.expectedIncome = expectedIncome;
        this.expectedExpense = expectedExpense;
    }

    public String getUsername() {
        return username;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public double getExpectedIncome() {
        return expectedIncome;
    }

    public double getExpectedExpense() {
        return expectedExpense;
    }
}
