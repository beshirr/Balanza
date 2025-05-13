package com.example.blanza;

public class FinancialEntity {
    protected int currentUserId;
    protected double amount;

    public FinancialEntity(int currentUserId, double amount) {
        this.currentUserId = currentUserId;
        this.amount = amount;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
