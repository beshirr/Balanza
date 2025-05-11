package com.example.blanza;

import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;

public class SignupController {
    public Button signup;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneNumberField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;


    @FXML
    private void handleSignup(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String email = emailField.getText();
        String phoneNumber = phoneNumberField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        if (UserManager.signupProcess(username, email, phoneNumber, password, confirmPassword)) {
            SceneController.switchScene("authentication.fxml", "Authentication");
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
