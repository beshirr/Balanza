package com.example.blanza;

import java.util.List;

public class ExpenseManager extends FinancialEntityManager<Expense> {
    private final ExpenseDB db = new ExpenseDB();
    
    @Override
    protected boolean validate(Expense expense) {
        return expense.getAmount() >= 0;
    }

    @Override
    protected void saveToDatabase(Expense expense) {
        db.insertToDatabase(expense);
    }
    
    @Override
    protected List<Expense> loadFromDatabase() {
        return db.getAllFromDatabase();
    }
}