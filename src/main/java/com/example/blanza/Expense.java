import java.time.LocalDate;

public class Expense {
    private String category;
    private float amount;
    private LocalDate date;
    private String paymentMethod;

    public Expense(String category, float amount, LocalDate time, String paymentMethod) {
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
