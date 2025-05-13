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
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller class for the expense reports view in the Balanza application.
 * <p>
 * This controller manages the expense reporting dashboard, which provides visual
 * and statistical analysis of user expenses. The dashboard includes multiple charts,
 * statistical breakdowns, and insights about spending patterns. Users can filter the
 * data by different time periods and export reports to CSV format.
 * <p>
 * The controller utilizes JavaFX charts and controls to display:
 * <ul>
 *   <li>Pie charts for category and payment method distribution</li>
 *   <li>Bar charts for monthly expense trends</li>
 *   <li>Statistical summaries and key metrics</li>
 *   <li>Data-driven insights about spending patterns</li>
 * </ul>
 */
public class ExpenseReportsController {
    /** The expense manager that provides access to expense data */
    private ExpenseManager expenseManager;
    
    /** Formatter for displaying month and year in charts and reports */
    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");

    /**
     * Initializes the controller when the corresponding view is loaded.
     * <p>
     * This method:
     * <ul>
     *   <li>Creates an instance of ExpenseManager to access expense data</li>
     *   <li>Initializes the period selection dropdown with available time ranges</li>
     *   <li>Sets the last updated timestamp</li>
     *   <li>Loads the initial report data</li>
     * </ul>
     */
    @FXML
    private void initialize() {
        expenseManager = new ExpenseManager();
        
        
        periodComboBox.setItems(FXCollections.observableArrayList(
                "Last Month", 
                "Last 3 Months", 
                "Last 6 Months",
                "This Year",
                "All Time"
        ));
        periodComboBox.setValue("Last Month");
        
        
        updateLastUpdatedTime();
        
        
        loadReportData();
    }

    /**
     * Refreshes the report data and updates the last updated timestamp.
     * <p>
     * This method is called when the user clicks the refresh button.
     */
    @FXML
    private void handleRefresh() {
        updateLastUpdatedTime();
        loadReportData();
    }

    /**
     * Navigates back to the expense tracking screen.
     * <p>
     * This method is called when the user clicks the back button.
     *
     * @param actionEvent The event that triggered this handler
     * @throws IOException If the scene switching fails
     */
    @FXML
    private void handleBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("track_expenses.fxml", "Track Expenses");
    }
    
    /**
     * Exports the current expense report data to a CSV file.
     * <p>
     * This method:
     * <ul>
     *   <li>Opens a file save dialog for the user to choose a location</li>
     *   <li>Exports the filtered expense data to the selected file</li>
     *   <li>Shows a success notification or error message</li>
     * </ul>
     *
     * @param actionEvent The event that triggered this handler
     */
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

    /**
     * Updates the last updated timestamp label with the current time.
     */
    private void updateLastUpdatedTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a");
        lastUpdatedLabel.setText(LocalDateTime.now().format(timeFormatter));
    }

    /**
     * Exports a list of expenses to a CSV file.
     * <p>
     * The CSV includes columns for date, category, amount, and payment method.
     *
     * @param file The destination file
     * @param expenses The list of expenses to export
     * @throws IOException If an I/O error occurs while writing to the file
     */
    private void exportToCSV(File file, List<Expense> expenses) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            
            writer.write("Date,Category,Amount,Payment Method\n");
            
            
            for (Expense expense : expenses) {
                writer.write(String.format("%s,%s,%.2f,%s\n",
                        expense.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        expense.getCategory(),
                        expense.getAmount(),
                        expense.getPaymentMethod()));
            }
        }
    }

    /**
     * Loads all report data based on the selected time period.
     * <p>
     * This method:
     * <ul>
     *   <li>Gets filtered expenses based on the selected period</li>
     *   <li>Updates all statistical summaries and metrics</li>
     *   <li>Populates all charts with the filtered data</li>
     *   <li>Generates data-driven insights about spending patterns</li>
     * </ul>
     */
    private void loadReportData() {
        List<Expense> filteredExpenses = getFilteredExpenses();
        
        
        float total = calculateTotal(filteredExpenses);
        totalExpensesLabel.setText(String.format("$%.2f", total));
        
        
        transactionCountLabel.setText(String.valueOf(filteredExpenses.size()));
        
        
        float avgTransaction = filteredExpenses.isEmpty() ? 0 : total / filteredExpenses.size();
        avgTransactionLabel.setText(String.format("$%.2f", avgTransaction));
        
        
        loadCategoryPieChart(filteredExpenses);
        
        
        loadPaymentMethodPieChart(filteredExpenses);
        
        
        loadMonthlyBarChart(filteredExpenses);
        
        
        loadCategoryStatsGrid(filteredExpenses);
        
        
        loadPaymentMethodStatsGrid(filteredExpenses);
        
        
        generateInsights(filteredExpenses);
    }

    /**
     * Gets expenses filtered by the selected time period.
     * <p>
     * The time periods available are:
     * <ul>
     *   <li>Last Month - expenses from the past 30 days</li>
     *   <li>Last 3 Months - expenses from the past 90 days</li>
     *   <li>Last 6 Months - expenses from the past 180 days</li>
     *   <li>This Year - expenses from January 1st of the current year</li>
     *   <li>All Time - all recorded expenses</li>
     * </ul>
     *
     * @return A filtered list of expenses based on the selected time period
     */
    private List<Expense> getFilteredExpenses() {
        String selectedPeriod = periodComboBox.getValue();
        LocalDate startDate = LocalDate.now();
        
        
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
                startDate = LocalDate.of(2000, 1, 1); 
                break;
        }
        
        
        LocalDate finalStartDate = startDate;
        return expenseManager.getAll().stream()
                .filter(expense -> expense.getDate().isAfter(finalStartDate) || expense.getDate().isEqual(finalStartDate))
                .collect(Collectors.toList());
    }
    
    /**
     * Calculates the total amount of all expenses in the provided list.
     *
     * @param expenses The list of expenses to calculate the total for
     * @return The total amount as a float
     */
    private float calculateTotal(List<Expense> expenses) {
        return (float) expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * Loads the category pie chart with data from the filtered expenses.
     * <p>
     * This method:
     * <ul>
     *   <li>Calculates the total amount spent per category</li>
     *   <li>Creates pie chart segments for each category</li>
     *   <li>Adds interactive labels and tooltips to the chart</li>
     * </ul>
     *
     * @param expenses The filtered list of expenses to display in the chart
     */
    private void loadCategoryPieChart(List<Expense> expenses) {
        Map<String, Double> categoryTotals = new HashMap<>();
        
        
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0d) + expense.getAmount());
        }
        
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        
        categoryPieChart.setData(pieChartData);
        categoryPieChart.setTitle("Expenses by Category");
        
        
        addPieChartLabels(pieChartData);
    }

    /**
     * Loads the payment method pie chart with data from the filtered expenses.
     * <p>
     * This method:
     * <ul>
     *   <li>Calculates the total amount spent per payment method</li>
     *   <li>Creates pie chart segments for each payment method</li>
     *   <li>Adds interactive labels and tooltips to the chart</li>
     * </ul>
     *
     * @param expenses The filtered list of expenses to display in the chart
     */
    private void loadPaymentMethodPieChart(List<Expense> expenses) {
        Map<String, Double> paymentMethodTotals = new HashMap<>();
        
        
        for (Expense expense : expenses) {
            String paymentMethod = expense.getPaymentMethod();
            paymentMethodTotals.put(paymentMethod, paymentMethodTotals.getOrDefault(paymentMethod, 0d) + expense.getAmount());
        }
        
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry : paymentMethodTotals.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        
        paymentMethodPieChart.setData(pieChartData);
        paymentMethodPieChart.setTitle("Expenses by Payment Method");
        
        
        addPieChartLabels(pieChartData);
    }
    
    /**
     * Adds interactive labels and tooltips to pie chart segments.
     * <p>
     * This method:
     * <ul>
     *   <li>Calculates percentage values for each segment</li>
     *   <li>Creates tooltips showing the name, value, and percentage</li>
     *   <li>Adds hover effects for better user feedback</li>
     * </ul>
     *
     * @param pieChartData The data for the pie chart
     */
    private void addPieChartLabels(ObservableList<PieChart.Data> pieChartData) {
        
        double total = pieChartData.stream()
                .mapToDouble(PieChart.Data::getPieValue)
                .sum();
                
        
        pieChartData.forEach(data -> {
            String percentage = String.format("%.1f%%", (data.getPieValue() / total * 100));
            String value = String.format("$%.2f", data.getPieValue());
            
            
            javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(
                data.getName() + "\n" + value + " (" + percentage + ")");
            javafx.scene.control.Tooltip.install(data.getNode(), tooltip);
            
            
            data.getNode().setOnMouseEntered(e -> data.getNode().setStyle("-fx-opacity: 0.8;"));
            data.getNode().setOnMouseExited(e -> data.getNode().setStyle("-fx-opacity: 1;"));
        });
    }
    
    /**
     * Loads the monthly bar chart with data from the filtered expenses.
     * <p>
     * This method:
     * <ul>
     *   <li>Calculates the total amount spent per month</li>
     *   <li>Creates bar chart columns for each month</li>
     *   <li>Sorts months chronologically</li>
     *   <li>Updates monthly statistics (average, highest, lowest)</li>
     * </ul>
     *
     * @param expenses The filtered list of expenses to display in the chart
     */
    private void loadMonthlyBarChart(List<Expense> expenses) {
        Map<String, Double> monthlyTotals = new HashMap<>();
        
        
        for (Expense expense : expenses) {
            String monthYear = expense.getDate().format(monthFormatter);
            monthlyTotals.put(monthYear, monthlyTotals.getOrDefault(monthYear, 0d) + expense.getAmount());
        }
        
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Expenses");
        
        
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
        
        
        updateMonthlyStatistics(monthlyTotals, sortedMonths);
    }
    
    /**
     * Updates the monthly statistics labels with average, highest, and lowest spending months.
     *
     * @param monthlyTotals Map of month names to total amounts
     * @param sortedMonths List of months sorted chronologically
     */
    private void updateMonthlyStatistics(Map<String, Double> monthlyTotals, List<String> sortedMonths) {
        if (monthlyTotals.isEmpty()) {
            monthlyAverageLabel.setText("$0.00");
            highestMonthLabel.setText("N/A");
            lowestMonthLabel.setText("N/A");
            return;
        }
        
        
        double average = (double) monthlyTotals.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
        monthlyAverageLabel.setText(String.format("$%.2f", average));
        
        
        Map.Entry<String, Double> highestEntry = null;
        for (String month : sortedMonths) {
            if (highestEntry == null || monthlyTotals.get(month) > highestEntry.getValue()) {
                highestEntry = new AbstractMap.SimpleEntry<>(month, monthlyTotals.get(month));
            }
        }
        
        if (highestEntry != null) {
            highestMonthLabel.setText(highestEntry.getKey() + ": " + String.format("$%.2f", highestEntry.getValue()));
        }
        
        
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
    
    /**
     * Loads the category statistics grid with data from the filtered expenses.
     * <p>
     * This method:
     * <ul>
     *   <li>Calculates total amount spent per category</li>
     *   <li>Sorts categories by total amount (descending)</li>
     *   <li>Displays top categories with amounts and percentages</li>
     * </ul>
     *
     * @param expenses The filtered list of expenses to analyze
     */
    private void loadCategoryStatsGrid(List<Expense> expenses) {
        
        categoryStatsGrid.getChildren().clear();
        
        if (expenses.isEmpty()) {
            Label noDataLabel = new Label("No data available");
            categoryStatsGrid.add(noDataLabel, 0, 0, 2, 1);
            return;
        }
        
        
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0d) + expense.getAmount());
        }
        
        
        List<Map.Entry<String, Double>> sortedEntries = categoryTotals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .toList();
        
        
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
    
    /**
     * Loads the payment method statistics grid with data from the filtered expenses.
     * <p>
     * This method:
     * <ul>
     *   <li>Calculates total amount spent per payment method</li>
     *   <li>Sorts payment methods by total amount (descending)</li>
     *   <li>Displays all payment methods with amounts and percentages</li>
     * </ul>
     *
     * @param expenses The filtered list of expenses to analyze
     */
    private void loadPaymentMethodStatsGrid(List<Expense> expenses) {
        
        paymentStatsGrid.getChildren().clear();
        
        if (expenses.isEmpty()) {
            Label noDataLabel = new Label("No data available");
            paymentStatsGrid.add(noDataLabel, 0, 0, 2, 1);
            return;
        }
        
        
        Map<String, Double> paymentTotals = new HashMap<>();
        for (Expense expense : expenses) {
            String paymentMethod = expense.getPaymentMethod();
            paymentTotals.put(paymentMethod, paymentTotals.getOrDefault(paymentMethod, 0d) + expense.getAmount());
        }
        
        
        List<Map.Entry<String, Double>> sortedEntries = paymentTotals.entrySet().stream().
                sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toList());
        
        
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
    
    /**
     * Generates data-driven insights about spending patterns.
     * <p>
     * This method:
     * <ul>
     *   <li>Analyzes category distributions and identifies top categories</li>
     *   <li>Examines payment method usage and preferences</li>
     *   <li>Identifies trends and patterns when sufficient data is available</li>
     *   <li>Generates user-friendly insights as text</li>
     * </ul>
     *
     * @param expenses The filtered list of expenses to analyze
     */
    private void generateInsights(List<Expense> expenses) {
        if (expenses.isEmpty()) {
            categoryInsightsLabel.setText("No expense data available for the selected period.");
            paymentInsightsLabel.setText("No payment data available for the selected period.");
            return;
        }
        
        
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense expense : expenses) {
            categoryTotals.put(expense.getCategory(), 
                categoryTotals.getOrDefault(expense.getCategory(), 0d) + expense.getAmount());
        }
        
        
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
        
        
        Map<String, Double> paymentTotals = new HashMap<>();
        Map<String, Integer> paymentCounts = new HashMap<>();
        
        for (Expense expense : expenses) {
            String method = expense.getPaymentMethod();
            paymentTotals.put(method, paymentTotals.getOrDefault(method, 0d) + expense.getAmount());
            paymentCounts.put(method, paymentCounts.getOrDefault(method, 0) + 1);
        }
        
        
        Map.Entry<String, Integer> mostUsedMethod = paymentCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
                
        
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

    /** Pie chart showing expense distribution by category */
    @FXML private PieChart categoryPieChart;
    
    /** Pie chart showing expense distribution by payment method */
    @FXML private PieChart paymentMethodPieChart;
    
    /** Bar chart showing expense trends by month */
    @FXML private BarChart<String, Number> monthlyBarChart;
    
    /** Dropdown for selecting the time period for the report */
    @FXML private ComboBox<String> periodComboBox;
    
    /** Label displaying the total expenses for the selected period */
    @FXML private Label totalExpensesLabel;
    
    /** Label displaying when the report was last updated */
    @FXML private Label lastUpdatedLabel;
    
    /** Label displaying insights about spending categories */
    @FXML private Label categoryInsightsLabel;
    
    /** Label displaying insights about payment methods */
    @FXML private Label paymentInsightsLabel;
    
    /** Grid for displaying category statistics */
    @FXML private GridPane categoryStatsGrid;
    
    /** Grid for displaying payment method statistics */
    @FXML private GridPane paymentStatsGrid;
    
    /** Label displaying the monthly average spending */
    @FXML private Label monthlyAverageLabel;
    
    /** Label displaying the highest spending month and amount */
    @FXML private Label highestMonthLabel;
    
    /** Label displaying the lowest spending month and amount */
    @FXML private Label lowestMonthLabel;
    
    /** Label displaying the total number of transactions */
    @FXML private Label transactionCountLabel;
    
    /** Label displaying the average transaction amount */
    @FXML private Label avgTransactionLabel;
}