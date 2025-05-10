package com.example.blanza;

import java.time.LocalDate;

public class Expense {
    private int current_user_id;
    private String category;
    private float amount;
    private LocalDate date;
    private String paymentMethod;

    public Expense(int current_user_id, String category, float amount, LocalDate time, String paymentMethod) {
        this.current_user_id = current_user_id;
        this.category = category;
        this.amount = amount;
        this.date = time;
        this.paymentMethod = paymentMethod;
    }

    public int getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(int current_user_id) {
        this.current_user_id = current_user_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
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
