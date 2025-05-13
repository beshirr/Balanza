package com.example.blanza;

public class IncomeManager  extends FinancialEntityManager<Income> {
    private final IncomeDB db = new IncomeDB();
    @Override
    protected boolean validate(Income income) {
        return income.getAmount() >= 0;
    }

    @Override
    protected void saveToDatabase(Income income) {
        db.insertToDatabase(income);
    }
}
