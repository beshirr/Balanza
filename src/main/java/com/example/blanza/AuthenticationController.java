package com.example.blanza;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthenticationController {
    @FXML private TextField emailField;
    @FXML private TextField OTPField;

    @FXML
    private void handleVerify(ActionEvent event) {
        String email = emailField.getText();
        String OTP = OTPField.getText();
        UserInfo userInfo = UserDB.getUserInfoByEmail(email);
        if (userInfo.getOtp().equals(OTP)) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("Budgeting.fxml"));
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
            alert.setContentText("Incorrect OTP");
            alert.showAndWait();
        }
    }
}
