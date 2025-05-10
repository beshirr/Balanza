package com.example.blanza;
 import java.time.LocalDate;

public class Budgeting {

    private String category;
    private double budgetAmount;
    private double actual_spend;
    private double remaining_budget;
    private int user_id;

    public Budgeting(String category, double budgetAmount, double actual_spend, int user_id){
        this.category = category;
        this.budgetAmount = budgetAmount;
        this.actual_spend = 0.0;
        this.remaining_budget = budgetAmount;
        this.user_id = user_id;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public double getBudgetAmount() {
        return budgetAmount;
    }
    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }
    public double getActual_spend() {
        return actual_spend;
    }
    public void setActual_spend(double actual_spend) {
        this.actual_spend = actual_spend;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public double getRemaining_budget() {
        return remaining_budget;
    }
    public void setRemaining_budget(double remaining_budget) {
        this.remaining_budget = remaining_budget;
    }

    public void updateRemainingBudget() {
        this.remaining_budget = this.budgetAmount - this.actual_spend;
    }


}
