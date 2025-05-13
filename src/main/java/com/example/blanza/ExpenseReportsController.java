package com.example.blanza;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ExpenseReportsController {
    private ExpenseManager expenseManager;
    private DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");

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
        
        // Set current time for last updated
        updateLastUpdatedTime();
        
        // Load the report data
        loadReportData();
    }

    @FXML
    private void handleRefresh() {
        updateLastUpdatedTime();
        loadReportData();
    }

    @FXML
    private void handleBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("track_expenses.fxml", "Track Expenses");
    }
    
    @FXML
    private void handleExport(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Expense Report");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            fileChooser.setInitialFileName("expense_report_" + 
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".csv");
            
            File file = fileChooser.showSaveDialog(categoryPieChart.getScene().getWindow());
            
            if (file != null) {
                exportToCSV(file, getFilteredExpenses());
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export Successful");
                alert.setHeaderText(null);
                alert.setContentText("Report successfully exported to " + file.getName());
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to export report: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateLastUpdatedTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a");
        lastUpdatedLabel.setText(LocalDateTime.now().format(timeFormatter));
    }

    private void exportToCSV(File file, List<Expense> expenses) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Write header
            writer.write("Date,Category,Amount,Payment Method\n");
            
            // Write data
            for (Expense expense : expenses) {
                writer.write(String.format("%s,%s,%.2f,%s\n",
                        expense.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        expense.getCategory(),
                        expense.getAmount(),
                        expense.getPaymentMethod()));
            }
        }
    }

    private void loadReportData() {
        List<Expense> filteredExpenses = getFilteredExpenses();
        
        // Update the total
        float total = calculateTotal(filteredExpenses);
        totalExpensesLabel.setText(String.format("$%.2f", total));
        
        // Update transaction count
        transactionCountLabel.setText(String.valueOf(filteredExpenses.size()));
        
        // Update average transaction
        float avgTransaction = filteredExpenses.isEmpty() ? 0 : total / filteredExpenses.size();
        avgTransactionLabel.setText(String.format("$%.2f", avgTransaction));
        
        // Load the pie chart data for categories
        loadCategoryPieChart(filteredExpenses);
        
        // Load the pie chart data for payment methods
        loadPaymentMethodPieChart(filteredExpenses);
        
        // Load the bar chart data for monthly expenses
        loadMonthlyBarChart(filteredExpenses);
        
        // Load category stats grid
        loadCategoryStatsGrid(filteredExpenses);
        
        // Load payment method stats grid
        loadPaymentMethodStatsGrid(filteredExpenses);
        
        // Generate insights
        generateInsights(filteredExpenses);
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
        return expenseManager.getAll().stream()
                .filter(expense -> expense.getDate().isAfter(finalStartDate) || expense.getDate().isEqual(finalStartDate))
                .collect(Collectors.toList());
    }
    
    private float calculateTotal(List<Expense> expenses) {
        return (float) expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    private void loadCategoryPieChart(List<Expense> expenses) {
        Map<String, Double> categoryTotals = new HashMap<>();
        
        // Calculate totals by category
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0d) + expense.getAmount());
        }
        
        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        
        categoryPieChart.setData(pieChartData);
        categoryPieChart.setTitle("Expenses by Category");
        
        // Add labels to pie chart segments
        addPieChartLabels(pieChartData);
    }

    private void loadPaymentMethodPieChart(List<Expense> expenses) {
        Map<String, Double> paymentMethodTotals = new HashMap<>();
        
        // Calculate totals by payment method
        for (Expense expense : expenses) {
            String paymentMethod = expense.getPaymentMethod();
            paymentMethodTotals.put(paymentMethod, paymentMethodTotals.getOrDefault(paymentMethod, 0d) + expense.getAmount());
        }
        
        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : paymentMethodTotals.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        
        paymentMethodPieChart.setData(pieChartData);
        paymentMethodPieChart.setTitle("Expenses by Payment Method");
        
        // Add labels to pie chart segments
        addPieChartLabels(pieChartData);
    }
    
    private void addPieChartLabels(ObservableList<PieChart.Data> pieChartData) {
        // Calculate the total for percentage calculations
        double total = pieChartData.stream()
                .mapToDouble(PieChart.Data::getPieValue)
                .sum();
                
        // Add listeners to show value and percentage when hovering over chart segments
        pieChartData.forEach(data -> {
            String percentage = String.format("%.1f%%", (data.getPieValue() / total * 100));
            String value = String.format("$%.2f", data.getPieValue());
            
            // Create tooltip with name, value and percentage
            javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                data.getName() + "\n" + value + " (" + percentage + ")");
            javafx.scene.control.Tooltip.install(data.getNode(), tooltip);
            
            // Change appearance on hover for better user feedback
            data.getNode().setOnMouseEntered(e -> data.getNode().setStyle("-fx-opacity: 0.8;"));
            data.getNode().setOnMouseExited(e -> data.getNode().setStyle("-fx-opacity: 1;"));
        });
    }
    
    private void loadMonthlyBarChart(List<Expense> expenses) {
        Map<String, Double> monthlyTotals = new HashMap<>();
        
        // Calculate totals by month
        for (Expense expense : expenses) {
            String monthYear = expense.getDate().format(monthFormatter);
            monthlyTotals.put(monthYear, monthlyTotals.getOrDefault(monthYear, 0d) + expense.getAmount());
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
        
        // Update monthly statistics
        updateMonthlyStatistics(monthlyTotals, sortedMonths);
    }
    
    private void updateMonthlyStatistics(Map<String, Double> monthlyTotals, List<String> sortedMonths) {
        if (monthlyTotals.isEmpty()) {
            monthlyAverageLabel.setText("$0.00");
            highestMonthLabel.setText("N/A");
            lowestMonthLabel.setText("N/A");
            return;
        }
        
        // Calculate average
        double average = (double) monthlyTotals.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        monthlyAverageLabel.setText(String.format("$%.2f", average));
        
        // Find highest month
        Map.Entry<String, Double> highestEntry = null;
        for (String month : sortedMonths) {
            if (highestEntry == null || monthlyTotals.get(month) > highestEntry.getValue()) {
                highestEntry = new AbstractMap.SimpleEntry<>(month, monthlyTotals.get(month));
            }
        }
        
        if (highestEntry != null) {
            highestMonthLabel.setText(highestEntry.getKey() + ": " + String.format("$%.2f", highestEntry.getValue()));
        }
        
        // Find lowest month
        Map.Entry<String, Double> lowestEntry = null;
        for (String month : sortedMonths) {
            if (lowestEntry == null || monthlyTotals.get(month) < lowestEntry.getValue()) {
                lowestEntry = new AbstractMap.SimpleEntry<>(month, monthlyTotals.get(month));
            }
        }
        
        if (lowestEntry != null) {
            lowestMonthLabel.setText(lowestEntry.getKey() + ": " + String.format("$%.2f", lowestEntry.getValue()));
        }
    }
    
    private void loadCategoryStatsGrid(List<Expense> expenses) {
        // Clear the grid
        categoryStatsGrid.getChildren().clear();
        
        if (expenses.isEmpty()) {
            Label noDataLabel = new Label("No data available");
            categoryStatsGrid.add(noDataLabel, 0, 0, 2, 1);
            return;
        }
        
        // Calculate category totals
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0d) + expense.getAmount());
        }
        
        // Sort categories by total amount (descending)
        List<Map.Entry<String, Double>> sortedEntries = categoryTotals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .toList();
        
        // Add to grid
        float total = calculateTotal(expenses);
        int row = 0;
        for (Map.Entry<String, Double> entry : sortedEntries) {
            String category = entry.getKey();
            double amount = entry.getValue();
            double percentage = total > 0 ? (amount / total * 100) : 0;
            
            Label categoryLabel = new Label(category);
            categoryLabel.getStyleClass().add("stat-label");
            
            Label amountLabel = new Label(String.format("$%.2f (%.1f%%)", amount, percentage));
            amountLabel.getStyleClass().add("stat-value");
            
            categoryStatsGrid.add(categoryLabel, 0, row);
            categoryStatsGrid.add(amountLabel, 1, row);
            
            row++;
        }
    }
    
    private void loadPaymentMethodStatsGrid(List<Expense> expenses) {
        // Clear the grid
        paymentStatsGrid.getChildren().clear();
        
        if (expenses.isEmpty()) {
            Label noDataLabel = new Label("No data available");
            paymentStatsGrid.add(noDataLabel, 0, 0, 2, 1);
            return;
        }
        
        // Calculate payment method totals
        Map<String, Double> paymentTotals = new HashMap<>();
        for (Expense expense : expenses) {
            String paymentMethod = expense.getPaymentMethod();
            paymentTotals.put(paymentMethod, paymentTotals.getOrDefault(paymentMethod, 0d) + expense.getAmount());
        }
        
        // Sort payment methods by total amount (descending)
        List<Map.Entry<String, Double>> sortedEntries = paymentTotals.entrySet().stream().
                sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toList());
        
        // Add to grid
        double total = calculateTotal(expenses);
        int row = 0;
        for (Map.Entry<String, Double> entry : sortedEntries) {
            String paymentMethod = entry.getKey();
            double amount = entry.getValue();
            double percentage = total > 0 ? (amount / total * 100) : 0;
            
            Label methodLabel = new Label(paymentMethod);
            methodLabel.getStyleClass().add("stat-label");
            
            Label amountLabel = new Label(String.format("$%.2f (%.1f%%)", amount, percentage));
            amountLabel.getStyleClass().add("stat-value");
            
            paymentStatsGrid.add(methodLabel, 0, row);
            paymentStatsGrid.add(amountLabel, 1, row);
            
            row++;
        }
    }
    
    private void generateInsights(List<Expense> expenses) {
        if (expenses.isEmpty()) {
            categoryInsightsLabel.setText("No expense data available for the selected period.");
            paymentInsightsLabel.setText("No payment data available for the selected period.");
            return;
        }
        
        // Generate category insights
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            categoryTotals.put(expense.getCategory(), 
                categoryTotals.getOrDefault(expense.getCategory(), 0d) + expense.getAmount());
        }
        
        // Find highest category
        Map.Entry<String, Double> highestCategory = categoryTotals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
                
        if (highestCategory != null) {
            double total = calculateTotal(expenses);
            double percentage = (highestCategory.getValue() / total) * 100;
            
            StringBuilder insight = new StringBuilder();
            insight.append(String.format("Your biggest expense category is %s at $%.2f, ", 
                highestCategory.getKey(), highestCategory.getValue()));
            insight.append(String.format("representing %.1f%% of your total spending. ", percentage));
            
            // Add trend if we have enough data points
            if (expenses.size() > 10) {
                LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
                
                double recentTotal = expenses.stream()
                    .filter(e -> e.getDate().isAfter(thirtyDaysAgo))
                    .map(Expense::getAmount)
                    .reduce(0d, Double::sum);
                    
                double recentCategoryTotal = expenses.stream()
                    .filter(e -> e.getDate().isAfter(thirtyDaysAgo) && 
                           e.getCategory().equals(highestCategory.getKey()))
                    .map(Expense::getAmount)
                    .reduce(0d, Double::sum);
                
                double recentPercentage = recentTotal > 0 ? (recentCategoryTotal / recentTotal) * 100 : 0;
                
                if (recentPercentage > percentage + 5) {
                    insight.append(String.format("This category has been trending upward recently."));
                } else if (recentPercentage < percentage - 5) {
                    insight.append(String.format("This category has been decreasing as a proportion of your spending recently."));
                }
            }
            
            categoryInsightsLabel.setText(insight.toString());
        } else {
            categoryInsightsLabel.setText("No category insights available.");
        }
        
        // Generate payment method insights
        Map<String, Double> paymentTotals = new HashMap<>();
        Map<String, Integer> paymentCounts = new HashMap<>();
        
        for (Expense expense : expenses) {
            String method = expense.getPaymentMethod();
            paymentTotals.put(method, paymentTotals.getOrDefault(method, 0d) + expense.getAmount());
            paymentCounts.put(method, paymentCounts.getOrDefault(method, 0) + 1);
        }
        
        // Find most used payment method (by count)
        Map.Entry<String, Integer> mostUsedMethod = paymentCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
                
        // Find highest payment method (by amount)
        Map.Entry<String, Double> highestPaymentMethod = paymentTotals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
                
        if (mostUsedMethod != null && highestPaymentMethod != null) {
            StringBuilder insight = new StringBuilder();
            
            if (mostUsedMethod.getKey().equals(highestPaymentMethod.getKey())) {
                insight.append(String.format("You primarily use %s for payments, ", mostUsedMethod.getKey()));
                insight.append(String.format("accounting for %d out of %d transactions (%d%%). ", 
                    mostUsedMethod.getValue(), expenses.size(), 
                    (mostUsedMethod.getValue() * 100 / expenses.size())));
            } else {
                insight.append(String.format("You use %s most frequently (%d times), ", 
                    mostUsedMethod.getKey(), mostUsedMethod.getValue()));
                insight.append(String.format("but spend the most with %s ($%.2f). ", 
                    highestPaymentMethod.getKey(), highestPaymentMethod.getValue()));
            }
            
            paymentInsightsLabel.setText(insight.toString());
        } else {
            paymentInsightsLabel.setText("No payment method insights available.");
        }
    }

    @FXML private PieChart categoryPieChart;
    @FXML private PieChart paymentMethodPieChart;
    @FXML private BarChart<String, Number> monthlyBarChart;
    @FXML private ComboBox<String> periodComboBox;
    @FXML private Label totalExpensesLabel;
    @FXML private Label lastUpdatedLabel;
    @FXML private Label categoryInsightsLabel;
    @FXML private Label paymentInsightsLabel;
    @FXML private GridPane categoryStatsGrid;
    @FXML private GridPane paymentStatsGrid;
    @FXML private Label monthlyAverageLabel;
    @FXML private Label highestMonthLabel;
    @FXML private Label lowestMonthLabel;
    @FXML private Label transactionCountLabel;
    @FXML private Label avgTransactionLabel;
}