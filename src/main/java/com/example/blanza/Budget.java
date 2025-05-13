package com.example.blanza;

public class Budget extends FinancialEntity {
    private String category;
    private double actual_spend;
    private double remaining_budget;

    public Budget(String category, double amount, double actual_spend, int user_id) {
        super(user_id, amount);
        this.category = category;
        this.actual_spend = 0.0;
        this.remaining_budget = amount;
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
}
