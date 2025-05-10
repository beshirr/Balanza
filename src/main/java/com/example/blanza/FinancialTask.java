public class FinancialTask {
    private String type;
    private float amount;

    public FinancialTask(String type, float amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() { return type; }
    public float getAmount() { return amount; }
}
