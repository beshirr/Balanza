package com.example.blanza;

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
}
