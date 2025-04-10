package com.example.budgettracker.model;

public class CategoryBudget {
    private String username;
    private String category;
    private int month;
    private int year;
    private double budgetAmount;

    public CategoryBudget(String username, String category, int month, int year, double budgetAmount) {
        this.username = username;
        this.category = category;
        this.month = month;
        this.year = year;
        this.budgetAmount = budgetAmount;
    }

    public String getUsername() {
        return username;
    }

    public String getCategory() {
        return category;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }
}
