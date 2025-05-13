package com.example.blanza;

import java.time.LocalDate;

public class Expense extends FinancialEntity {
    private String category;
    private LocalDate date;
    private String paymentMethod;

    public Expense(int current_user_id, String category, float amount, LocalDate time, String paymentMethod) {
        super(current_user_id, amount);
        this.category = category;
        this.amount = amount;
        this.date = time;
        this.paymentMethod = paymentMethod;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
