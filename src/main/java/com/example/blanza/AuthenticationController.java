package com.example.blanza;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.io.IOException;

/**
 * Controller class that handles the authentication processes in the application.
 * 
 * This controller manages the OTP verification workflow and navigates to the appropriate
 * scenes based on the authentication status.
 */
public class AuthenticationController {
    /** TextField for collecting user's email address. */
    @FXML private TextField emailField;
    
    /** TextField for collecting user's One-Time Password (OTP). */
    @FXML private TextField OTPField;

    /**
     * Handles the verification of OTP entered by the user.
     * 
     * This method extracts the OTP from the input field, verifies it using the 
     * AuthenticationService, and takes appropriate action based on the result.
     * If the OTP is valid, the user's verification status is updated in the database
     * and the user is redirected to the home screen. Otherwise, an error alert is displayed.
     *
     * @param event The action event triggered by the verify button click
     * @throws IOException If there is an error while switching scenes
     */
    @FXML
    private void handleVerify(ActionEvent event) throws IOException {
        String OTP = OTPField.getText();
        if (AuthenticationService.verifyOTP(OTP)) {
            UserDB.setVerified(SessionManager.loadSession());
            SceneController.switchScene("home.fxml", "Balanza");
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect OTP");
            alert.showAndWait();
        }
    }
}