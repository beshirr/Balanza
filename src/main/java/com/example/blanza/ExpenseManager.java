package com.example.blanza;

public class ExpenseManager extends FinancialEntityManager<Expense> {
    @Override
    protected boolean validate(Expense expense) {
        return expense.getAmount() >= 0;
    }

    @Override
    protected void saveToDatabase(Expense expense) {
        ExpenseDB.insertExpense(expense);
    }
}
