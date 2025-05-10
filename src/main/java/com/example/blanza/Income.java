import java.util.*;

public class Income {
    private String income_source;
    private float amount;
    private float total_income;
    private String pay_date;
    private int user_id;


    public Income(String source , float value )
    {
        this.income_source = source;
        this.amount = value;
    }
    public float getTotalIncome(){
        return total_income;
    }
    public void setTotalIncome(float total){
        this.total_income = total;
    }
    public String getIncomeSource(){
        return income_source;

    }
    public void setIncomeSource(String source){
        this.income_source = source;
    }
    public float getAmount(){
        return amount;
    }
    public void setAmount(float value){
        this.amount = value;
    }
    public String getPay_date(){
        return pay_date;
    }
    public void setPay_date(String date){
        this.pay_date = date;

    }
    public int getUser_id(){
        return user_id;
    }
    public void setUser_id(int id){
        this.user_id = id;
    }








}
