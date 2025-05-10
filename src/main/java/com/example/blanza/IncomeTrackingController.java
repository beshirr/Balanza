package com.example.blanza;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class IncomeTrackingController {

    @FXML
    private TextField sourceField;

    @FXML
    private TextField amountField;

    @FXML
    private Label totalIncomeLabel;

    private Income income;


    public void initialize() {
        income = new Income("", 0.0, LocalDate.now()); // Create a new Income object with default values
    }


    @FXML
    public void handleSaveIncome() {
        String source = sourceField.getText();
        String amountText = amountField.getText();

        try {

            double amount = Double.parseDouble(amountText);
            income.setIncomeSource(source);
            income.setAmount(amount);
            income.updateTotalIncome();


            totalIncomeLabel.setText("Total Income: " + income.getTotalIncome());

            sourceField.clear();
            amountField.clear();

        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid amount entered.");
            alert.show();
        }
    }
}
