package com.example.budgettracker.controllers;
import com.example.budgettracker.service.ExpectedBudgetService;

import com.example.budgettracker.model.ExpectedBudget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.budgettracker.model.CategoryBudget;
import com.example.budgettracker.service.CategoryBudgetService;
import com.example.budgettracker.factory.BudgetFactory;
import com.example.budgettracker.service.TransactionService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Controller
public class BudgetManagerController {

    private final ExpectedBudgetService expectedBudgetService;
    private final CategoryBudgetService categoryBudgetService;
    private final TransactionService transactionService;

    @Autowired
    public BudgetManagerController(ExpectedBudgetService expectedBudgetService,
                                   CategoryBudgetService categoryBudgetService,
                                   TransactionService transactionService) {
        this.expectedBudgetService = expectedBudgetService;
        this.categoryBudgetService = categoryBudgetService;
        this.transactionService = transactionService;
    }
    @GetMapping("/budget-manager")
public String showBudgetManager(@RequestParam String username, Model model) {
    System.out.println("Rendering budget manager for: " + username);

    model.addAttribute("username", username);

    // Current month and year
    LocalDate now = LocalDate.now();
    int month = now.getMonthValue();
    int year = now.getYear();

    ExpectedBudget expected = expectedBudgetService.getBudgetForUserAndMonth(username, month, year);
    double actualIncome = transactionService.getTotalIncomeForMonth(username, month, year);
    double actualExpense = transactionService.getTotalExpenseForMonth(username, month, year);
    
model.addAttribute("expectedIncome", expected != null ? expected.getExpectedIncome() : 0.0);
model.addAttribute("expectedExpense", expected != null ? expected.getExpectedExpense() : 0.0);
model.addAttribute("actualIncome", actualIncome);
model.addAttribute("actualExpense", actualExpense);


    List<CategoryBudget> budgets = categoryBudgetService.getBudgetsForUserAndMonth(username, month, year);
    Map<String, String> budgetStatus = new LinkedHashMap<>();

    for (CategoryBudget cb : budgets) {
        double spent = transactionService.getTotalSpentForCategory(username, cb.getCategory(), month, year);
        String status = spent <= cb.getBudgetAmount()
            ? "✅ Under Budget (" + spent + " / " + cb.getBudgetAmount() + ")"
            : "❌ Over Budget (" + spent + " / " + cb.getBudgetAmount() + ")";
        budgetStatus.put(cb.getCategory(), status);
    }

    model.addAttribute("budgetStatus", budgetStatus);

    return "budget-manager";
}

    @PostMapping("/budget-manager/setExpectedBudget")
    public String handleExpectedBudget(
        @RequestParam String username,
        @RequestParam int month,
        @RequestParam int year,
        @RequestParam double expectedIncome,
        @RequestParam double expectedExpense,
        Model model
    ) {
        ExpectedBudget budget = BudgetFactory.createExpectedBudget(username, month, year, expectedIncome, expectedExpense);

        expectedBudgetService.saveExpectedBudget(budget);  // ✅ use the service!

        model.addAttribute("username", username);
        model.addAttribute("message", "Expected Budget saved successfully!");
        return "budget-manager";
    }
    @PostMapping("/budget-manager/setCategoryBudget")
public String handleCategoryBudget(
    @RequestParam String username,
    @RequestParam String category,
    @RequestParam int month,
    @RequestParam int year,
    @RequestParam double amount,
    Model model
) {
    CategoryBudget cb = BudgetFactory.createCategoryBudget(username, category, month, year, amount);

    categoryBudgetService.saveCategoryBudget(cb);

    model.addAttribute("username", username);
    model.addAttribute("message", "Category Budget saved successfully!");
    return "budget-manager";
}

}
