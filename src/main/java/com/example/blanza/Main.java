package com.example.blanza;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        UserDB.createUserDB();
        Parent root;
        int userId = SessionManager.loadSession();
        if (userId != -1) {
            Session.setCurrentUserId(userId);
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("income.fxml")));
        } else {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("index.fxml")));
        }

        SceneController.setStage(stage);
        stage.setTitle("BALANZA");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}