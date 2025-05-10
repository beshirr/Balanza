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
        SceneController.setStage(stage);

        //مؤقتا كل واحد يحط اسم الفايل اللي شغال عليه
        // محدش يرفع الفايل دا على جت لحد ما نخلص الفايللت كلها و نربطهم
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Income.fxml")));
        stage.setTitle("BALANZA");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}