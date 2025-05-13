package com.example.blanza;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class IncomeTrackingController {

    @FXML
    private TextField sourceField;

    @FXML
    private TextField amountField;

    @FXML
    private Label totalIncomeLabel;
    
    @FXML
    private TableView<Income> incomeTable;
    
    @FXML
    private TableColumn<Income, String> sourceColumn;
    
    @FXML
    private TableColumn<Income, Double> amountColumn;
    
    @FXML
    private TableColumn<Income, LocalDate> dateColumn;

    private final IncomeDB incomeDB = new IncomeDB();
    private final ObservableList<Income> incomeList = FXCollections.observableArrayList();

    public void initialize() {
        // Set up the table columns
        sourceColumn.setCellValueFactory(new PropertyValueFactory<>("income_source"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("pay_date"));
        
        // Load incomes from database
        loadIncomes();
        
        // Set table items
        incomeTable.setItems(incomeList);
        
        // Update total income display
        updateTotalIncomeDisplay();
    }
    
    private void loadIncomes() {
        try {
            List<Income> incomes = incomeDB.getAllFromDatabase();
            incomeList.clear();
            incomeList.addAll(incomes);
        } catch (Exception e) {
            System.err.println("Error loading incomes: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error loading income data");
        }
    }

    private void updateTotalIncomeDisplay() {
        double total = incomeList.stream()
                .mapToDouble(Income::getAmount)
                .sum();
        totalIncomeLabel.setText(String.format("$%.2f", total));
    }

    @FXML
    public void handleSaveIncome() {
        String source = sourceField.getText();
        String amountText = amountField.getText();

        if (source == null || source.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please enter an income source");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Amount must be greater than zero");
                return;
            }
            
            // Create new income with current date
            Income newIncome = new Income(
                SessionService.getCurrentUserId(),
                source,
                amount,
                LocalDate.now()
            );
            
            // Save to database
            incomeDB.insertToDatabase(newIncome);
            
            // Refresh the income list
            loadIncomes();
            
            // Update total
            updateTotalIncomeDisplay();
            
            // Clear fields
            sourceField.clear();
            amountField.clear();
            
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Invalid amount entered");
        } catch (Exception e) {
            System.err.println("Error saving income: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error saving income");
        }
    }

    @FXML
    public void handleBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("home.fxml", "Balanza");
    }
    
    private void showAlert(Alert.AlertType type, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}