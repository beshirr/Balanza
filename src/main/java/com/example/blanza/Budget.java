package com.example.blanza;

public class Budget extends FinancialEntity {
    private String category;
    private double actual_spend;
    private double remaining_budget;
    private int id; // Add a field for the database ID

    public Budget(String category, double amount, double actual_spend, int user_id) {
        super(user_id, amount);
        this.category = category;
        this.actual_spend = actual_spend;
        this.remaining_budget = amount - actual_spend;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getActual_spend() {
        return actual_spend;
    }

    public void setActual_spend(double actual_spend) {
        this.actual_spend = actual_spend;
    }

    public double getRemaining_budget() {
        return remaining_budget;
    }

    public void setRemaining_budget(double remaining_budget) {
        this.remaining_budget = remaining_budget;
    }

    public void updateRemainingBudget() {
        this.remaining_budget = this.amount - this.actual_spend;
    }
    
    // Add getters and setters for the ID field
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}