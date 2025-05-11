package com.example.blanza;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        UserDB.createUserDB();
        ExpenseDB.createExpenseTable();
        IncomeDB.createIncomeTable();
        BudgetDB.createBudgetTable();
    }
}