package com.example.blanza;

public class BudgetManager extends FinancialEntityManager<Budget> {
    private final BudgetDB db = new BudgetDB();
    @Override
    protected boolean validate(Budget budget) {
        return budget.getAmount() >= 0;
    }

    @Override
    protected void saveToDatabase(Budget budget) {
        db.insertToDatabase(budget);
    }
}
