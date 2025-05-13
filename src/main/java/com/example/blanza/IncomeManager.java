package com.example.blanza;

import java.util.List;

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
    @Override
    protected List<Income> loadFromDatabase() {
        return db.getAllFromDatabase();
    }
}
