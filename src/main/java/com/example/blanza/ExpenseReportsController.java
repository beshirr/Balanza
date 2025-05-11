package com.example.blanza;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseReportsController {

    @FXML
    private PieChart categoryPieChart;

    @FXML
    private PieChart paymentMethodPieChart;

    @FXML
    private BarChart<String, Number> monthlyBarChart;

    @FXML
    private ComboBox<String> periodComboBox;

    @FXML
    private Label totalExpensesLabel;

    private ExpenseManager expenseManager;

    @FXML
    private void initialize() {
        expenseManager = new ExpenseManager();
        
        // Initialize the period combo box
        periodComboBox.setItems(FXCollections.observableArrayList(
                "Last Month", 
                "Last 3 Months", 
                "Last 6 Months",
                "This Year",
                "All Time"
        ));
        periodComboBox.setValue("Last Month");
        
        // Load the report data
        loadReportData();
    }

    @FXML
    private void handleRefresh() {
        loadReportData();
    }

    @FXML
    private void handleBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("track_expenses.fxml", "Track Expenses");
    }

    private void loadReportData() {
        List<Expense> filteredExpenses = getFilteredExpenses();
        
        // Update the total
        float total = calculateTotal(filteredExpenses);
        totalExpensesLabel.setText(String.format("$%.2f", total));
        
        // Load the pie chart data for categories
        loadCategoryPieChart(filteredExpenses);
        
        // Load the pie chart data for payment methods
        loadPaymentMethodPieChart(filteredExpenses);
        
        // Load the bar chart data for monthly expenses
        loadMonthlyBarChart(filteredExpenses);
    }

    private List<Expense> getFilteredExpenses() {
        String selectedPeriod = periodComboBox.getValue();
        LocalDate startDate = LocalDate.now();
        
        // Determine the start date based on selected period
        switch (selectedPeriod) {
            case "Last Month":
                startDate = startDate.minusMonths(1);
                break;
            case "Last 3 Months":
                startDate = startDate.minusMonths(3);
                break;
            case "Last 6 Months":
                startDate = startDate.minusMonths(6);
                break;
            case "This Year":
                startDate = LocalDate.of(startDate.getYear(), 1, 1);
                break;
            case "All Time":
                startDate = LocalDate.of(2000, 1, 1); // Far in the past
                break;
        }
        
        // Filter expenses by date
        LocalDate finalStartDate = startDate;
        return expenseManager.getExpenses().stream()
                .filter(expense -> expense.getDate().isAfter(finalStartDate) || expense.getDate().isEqual(finalStartDate))
                .collect(Collectors.toList());
    }
    
    private float calculateTotal(List<Expense> expenses) {
        return (float) expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    private void loadCategoryPieChart(List<Expense> expenses) {
        Map<String, Float> categoryTotals = new HashMap<>();
        
        // Calculate totals by category
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0f) + expense.getAmount());
        }
        
        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " ($" + String.format("%.2f", entry.getValue()) + ")", entry.getValue()));
        }
        
        categoryPieChart.setData(pieChartData);
        categoryPieChart.setTitle("Expenses by Category");
    }

    private void loadPaymentMethodPieChart(List<Expense> expenses) {
        Map<String, Float> paymentMethodTotals = new HashMap<>();
        
        // Calculate totals by payment method
        for (Expense expense : expenses) {
            String paymentMethod = expense.getPaymentMethod();
            paymentMethodTotals.put(paymentMethod, paymentMethodTotals.getOrDefault(paymentMethod, 0f) + expense.getAmount());
        }
        
        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Float> entry : paymentMethodTotals.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey() + " ($" + String.format("%.2f", entry.getValue()) + ")", entry.getValue()));
        }
        
        paymentMethodPieChart.setData(pieChartData);
        paymentMethodPieChart.setTitle("Expenses by Payment Method");
    }
    
    private void loadMonthlyBarChart(List<Expense> expenses) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        Map<String, Float> monthlyTotals = new HashMap<>();
        
        // Calculate totals by month
        for (Expense expense : expenses) {
            String monthYear = expense.getDate().format(formatter);
            monthlyTotals.put(monthYear, monthlyTotals.getOrDefault(monthYear, 0f) + expense.getAmount());
        }
        
        // Create bar chart data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Expenses");
        
        // Sort keys by date
        List<String> sortedMonths = monthlyTotals.keySet().stream()
                .sorted((m1, m2) -> {
                    try {
                        return LocalDate.parse("01 " + m1, DateTimeFormatter.ofPattern("dd MMM yyyy"))
                                .compareTo(LocalDate.parse("01 " + m2, DateTimeFormatter.ofPattern("dd MMM yyyy")));
                    } catch (Exception e) {
                        return m1.compareTo(m2);
                    }
                })
                .collect(Collectors.toList());
        
        for (String month : sortedMonths) {
            series.getData().add(new XYChart.Data<>(month, monthlyTotals.get(month)));
        }
        
        monthlyBarChart.getData().clear();
        monthlyBarChart.getData().add(series);
        monthlyBarChart.setTitle("Monthly Expense Trends");
    }
}