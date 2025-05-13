package com.example.blanza;

public class BudgetManager extends FinancialEntityManager<Budgeting> {
    @Override
    protected boolean validate(Budgeting budget) {
        return budget.getAmount() >= 0;
    }

    @Override
    protected void saveToDatabase(Budgeting budget) {
        BudgetDB.insertBudget(budget);
    }
}
