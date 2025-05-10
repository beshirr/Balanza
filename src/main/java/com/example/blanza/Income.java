package com.example.blanza;
import java.time.LocalDate;
import java.util.*;

public class Income {

    private String income_source;
    private double amount;
    private double total_income;
    private LocalDate pay_date;
    private int user_id;


    public Income(String source , double value , LocalDate pay_date)
    {
        this.income_source = source;
        this.amount = value;
        this.pay_date = pay_date;
        this.total_income = 0.0;

    }
    public double getTotalIncome(){
        return total_income;
    }
    public void setTotalIncome(double total){
        this.total_income = total;
    }
    public String getIncomeSource(){
        return income_source;
    }
    public void setIncomeSource(String source){
        this.income_source = source;
    }
    public double getAmount(){
        return amount;
    }
    public void setAmount(double value){
        this.amount = value;
    }
    public LocalDate getPay_date(){
        return pay_date;
    }
    public void setPay_date(LocalDate date){
        this.pay_date = date;
    }
    public int getUser_id(){
        return user_id;
    }
    public void setUser_id(int id){
        this.user_id = id;
    }


    public void updateTotalIncome() {
        this.total_income += this.amount;
    }








}
