package com.example.budgettracker.model; // Ensure correct package
import java.util.Date; // Import necessary class

public class IncomeTransaction extends Transaction {  
    public IncomeTransaction(double amount, Date date, String description) {
        super(amount, date, description);  // Call the parent constructor
    }

    @Override
    public String getDetails() {
        return String.format("[Income] Amount: $%.2f, Date: %s, Description: %s", 
                              amount, date.toString(), description);
    }
}
