package com.example.blanza;

import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {
    private final List<Expense> expenses;
    private final ExpenseDB db = new ExpenseDB();

    public ExpenseManager() {
        expenses = db.getAllExpenses();
    }

    public void addExpense(Expense expense) {
        if (validateExpense(expense)) {
            expenses.add(expense);
            updateBudget(expense);
            db.insertExpense(expense);
        }
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public float getTotalExpenses() {
        float total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    private boolean validateExpense(Expense expense) {
        return expense.getAmount() >= 0; // TODO: logic
    }

    private void updateBudget(Expense expense) {
        // TODO: Add logic
    }
}
