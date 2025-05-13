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
        DatabaseInitializer.initialize();
        Parent root;
        int userId = SessionManager.loadSession();
        if (userId != -1 && UserDB.getUserVerified(userId)) {
            SessionService.setCurrentUserId(userId);
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("home.fxml")));
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