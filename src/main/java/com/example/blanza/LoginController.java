package com.example.blanza;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * The type Login controller.
 */
public class LoginController {
    /**
     * The Login.
     */
    public Button login;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin(javafx.event.ActionEvent actionEvent) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        if (UserManager.loginProcess(email, password)) {
            SceneController.switchScene("home.fxml", "Balanza");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect email or password");
            alert.showAndWait();
        }
    }
}
