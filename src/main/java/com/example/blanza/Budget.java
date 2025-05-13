package com.example.blanza;

/**
 * Represents a budget category with tracking for allocated funds, actual spending, and remaining balance.
 * 
 * The Budget class extends FinancialEntity and provides functionality to manage 
 * category-specific budgeting, including tracking allocated amounts, actual expenditures,
 * and calculating remaining funds. Each budget is associated with a specific user and category.
 */
public class Budget extends FinancialEntity {
    /** The category name for this budget (e.g., "Groceries", "Entertainment", etc.) */
    private String category;
    
    /** The actual amount spent in this budget category */
    private double actual_spend;
    
    /** The remaining amount available in this budget (allocated amount - actual spend) */
    private double remaining_budget;
    
    /** The unique identifier for this budget record in the database */
    private int id;

    /**
     * Constructs a new Budget with the specified category, amount, actual spend, and user ID.
     * 
     * @param category     the category name for this budget
     * @param amount       the total allocated amount for this budget
     * @param actual_spend the amount already spent in this budget category
     * @param user_id      the ID of the user who owns this budget
     */
    public Budget(String category, double amount, double actual_spend, int user_id) {
        super(user_id, amount);
        this.category = category;
        this.actual_spend = actual_spend;
        this.remaining_budget = amount - actual_spend;
    }

    /**
     * Gets the category name of this budget.
     * 
     * @return the category name
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category name for this budget.
     * 
     * @param category the new category name
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the actual amount spent in this budget category.
     * 
     * @return the actual spend amount
     */
    public double getActual_spend() {
        return actual_spend;
    }

    /**
     * Sets the actual amount spent in this budget category.
     * Note: This does not automatically update the remaining budget.
     * 
     * @param actual_spend the new actual spend amount
     */
    public void setActual_spend(double actual_spend) {
        this.actual_spend = actual_spend;
    }

    /**
     * Gets the remaining amount available in this budget.
     * 
     * @return the remaining budget amount
     */
    public double getRemaining_budget() {
        return remaining_budget;
    }

    /**
     * Sets the remaining amount available in this budget.
     * Note: This is typically calculated automatically but can be manually set if needed.
     * 
     * @param remaining_budget the new remaining budget amount
     */
    public void setRemaining_budget(double remaining_budget) {
        this.remaining_budget = remaining_budget;
    }

    /**
     * Recalculates and updates the remaining budget based on the current allocated amount
     * and actual spend values.
     * 
     * This method should be called after any changes to the amount or actual_spend
     * to ensure the remaining_budget is synchronized.
     */
    public void updateRemainingBudget() {
        this.remaining_budget = this.amount - this.actual_spend;
    }
    
    /**
     * Gets the unique database identifier for this budget.
     * 
     * @return the budget's database ID
     */
    public int getId() {
        return id;
    }
    
    /**
     * Sets the unique database identifier for this budget.
     * 
     * @param id the database ID to set
     */
    public void setId(int id) {
        this.id = id;
    }
}