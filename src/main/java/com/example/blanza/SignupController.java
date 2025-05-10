package com.example.blanza;

import javafx.fxml.FXML;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class SignupController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneNumberField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;


    @FXML
    private void handleSignup(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (UserManager.signupProcess(username, email, phoneNumber, password, confirmPassword)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please check your credentials");
            alert.showAndWait();
        }
    }
}
