package com.example.blanza;

public class DatabaseInitializer {
    public static void initialize() {
        UserDB.createTable("create_user_table");
        ExpenseDB.createTable("create_expenses_table");
        BudgetDB.createTable("create_budget_table");
        IncomeDB.createTable("create_income_table");
    }
}
