package com.example.blanza;

import javafx.event.ActionEvent;

import java.io.IOException;

public class Home {
    public void handleExpensesBtn(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("track_expenses.fxml", "Track Expenses");
    }

    public void handleBudgetBtn(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("Budgeting.fxml", "Budgeting");
    }

    public void handleIncomeBtn(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("income.fxml", "Income");
    }
}
