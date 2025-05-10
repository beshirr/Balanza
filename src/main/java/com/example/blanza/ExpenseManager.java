package com.example.blanza;

import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {
    private List<Expense> expenses = new ArrayList<>();
    private final ExpenseDB db = new ExpenseDB();

    public void addExpense(Expense expense) {
        if (validateExpense(expense)) {
            expenses.add(expense);
            updateBudget(expense);
            db.insertExpense(expense);
        }
    }

    private boolean validateExpense(Expense expense) {
        return expense.getAmount() >= 0; // TODO: logic
    }

    private void updateBudget(Expense expense) {
        // TODO: Add logic
    }
}
