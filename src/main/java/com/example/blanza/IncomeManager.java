package com.example.blanza;

public class IncomeManager  extends FinancialEntityManager<Income> {
    @Override
    protected boolean validate(Income income) {
        return income.getAmount() >= 0;
    }

    @Override
    protected void saveToDatabase(Income income) {
        IncomeDB.insertIncome(income);
    }
}
