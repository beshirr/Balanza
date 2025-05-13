package com.example.blanza;

import java.time.LocalDate;

public class Income extends FinancialEntity {
    private String income_source;
    private double total_income;
    private LocalDate pay_date;

    public Income(int currentUserId, String source , double amount , LocalDate pay_date) {
        super(currentUserId, amount);
        this.income_source = source;
        this.pay_date = pay_date;
        this.total_income = 0.0;
    }

    public String getIncome_source() {
        return income_source;
    }

    public void setIncome_source(String income_source) {
        this.income_source = income_source;
    }

    public double getTotal_income() {
        return total_income;
    }

    public void setTotal_income(double total_income) {
        this.total_income = total_income;
    }

    public LocalDate getPay_date() {
        return pay_date;
    }

    public void setPay_date(LocalDate pay_date) {
        this.pay_date = pay_date;
    }

    public void updateTotalIncome() {
        this.total_income += this.amount;
    }
}
