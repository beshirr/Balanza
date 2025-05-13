package com.example.blanza;

public class DatabaseInitializer {
    public static void initialize() {
        Database.createTable("create_user_table");
        ExpenseDB.createTable("create_expenses_table");
        BudgetDB.createTable("create_budget_table");
        IncomeDB.createTable("create_income_table");
        ReminderDB.createTable("create_reminder_table");
        Database.createTable("create_financial_task_table");
    }
}
