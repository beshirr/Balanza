package com.example.blanza;

/**
 * Initializes all database tables required by the Balanza application.
 * <p>
 * This utility class is responsible for creating the necessary database tables
 * when the application starts. It calls the appropriate table creation methods
 * in the database classes, passing SQL query keys that are used to retrieve
 * the CREATE TABLE statements from the SQLLoader.
 * <p>
 * The tables created include:
 * <ul>
 *   <li>Users table - for storing user account information</li>
 *   <li>Expenses table - for storing user expense records</li>
 *   <li>Budget table - for storing budget configurations</li>
 *   <li>Income table - for storing income records</li>
 *   <li>Reminder table - for storing user reminders</li>
 *   <li>Financial Task table - for storing financial tasks</li>
 * </ul>
 * <p>
 * This class should be called once during application startup to ensure
 * all required database tables exist before data access operations begin.
 */
public class DatabaseInitializer {
    
    /**
     * Initializes all database tables for the application.
     * <p>
     * This method calls the createTable method on each database class,
     * passing the appropriate SQL query key. The SQL queries are stored
     * externally and retrieved by the SQLLoader using these keys.
     * <p>
     * If the tables already exist, the database will typically ignore
     * the creation request without error.
     */
    public static void initialize() {
        Database.createTable("create_user_table");
        ExpenseDB.createTable("create_expenses_table");
        BudgetDB.createTable("create_budget_table");
        IncomeDB.createTable("create_income_table");
        ReminderDB.createTable("create_reminder_table");
        FinancialTaskDB.createTable("create_financial_task_table");
    }
}