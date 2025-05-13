package com.example.blanza;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * A utility class for managing scene transitions in the Balanza application.
 * <p>
 * The SceneController centralizes the logic for navigating between different
 * screens (scenes) of the JavaFX application. It maintains a reference to the
 * primary stage and provides methods to switch between different FXML-defined
 * views while setting appropriate titles and dimensions.
 * <p>
 * This class follows the singleton pattern through static methods, ensuring
 * consistent scene navigation throughout the application without needing to
 * pass the Stage reference between controllers.
 * <p>
 * Using a centralized scene controller offers several benefits:
 * <ul>
 *   <li>Simplified navigation between screens</li>
 *   <li>Consistent scene dimensions and stage configuration</li>
 *   <li>Reduced code duplication in controller classes</li>
 *   <li>Single point of modification for window behavior</li>
 * </ul>
 * <p>
 * Example usage in a controller class:
 * <pre>{@code
 * @FXML
 * private void handleLogout() throws IOException {
 *     SceneController.switchScene("login.fxml", "Balanza - Login");
 * }
 * }</pre>
 *
 * @see javafx.stage.Stage
 * @see javafx.scene.Scene
 * @see javafx.fxml.FXMLLoader
 */
public class SceneController {
    
    /**
     * The primary stage of the application.
     * <p>
     * This static reference holds the main window stage of the JavaFX application.
     * It is set during application initialization and used for all scene transitions.
     */
    private static Stage primaryStage;

    /**
     * Sets the primary stage reference for the application.
     * <p>
     * This method should be called once during application startup, typically
     * in the main application class's start method. The stage reference is then
     * used by all subsequent scene transition operations.
     * <p>
     * Example usage in the main application class:
     * <pre>{@code
     * @Override
     * public void start(Stage primaryStage) {
     *     SceneController.setStage(primaryStage);
     *     // Initial scene setup
     *     try {
     *         SceneController.switchScene("login.fxml", "Balanza - Login");
     *     } catch (IOException e) {
     *         e.printStackTrace();
     *     }
     * }
     * }</pre>
     *
     * @param stage The primary Stage object from the JavaFX application
     */
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Switches the current scene to a new scene defined by the specified FXML file.
     * <p>
     * This method loads the specified FXML file, creates a new scene with default
     * dimensions (800x600), sets the window title, and displays the new scene in
     * the application's primary stage.
     * <p>
     * The method handles the entire scene transition process, including:
     * <ul>
     *   <li>Loading the FXML resource</li>
     *   <li>Creating a new Scene with the loaded layout</li>
     *   <li>Setting the window title</li>
     *   <li>Updating the primary stage with the new scene</li>
     *   <li>Making the window visible (if not already)</li>
     * </ul>
     * <p>
     * This centralized approach ensures consistent window dimensions and behavior
     * across all screen transitions.
     *
     * @param fxmlFile The name of the FXML file to load (e.g., "login.fxml")
     * @param title The title to display in the window's title bar
     * @throws IOException If the FXML file cannot be loaded or parsed
     * @throws IllegalStateException If called before the primary stage is set
     *         via the {@link #setStage(Stage)} method
     */
    public static void switchScene(String fxmlFile, String title) throws IOException {
        if (primaryStage == null) {
            throw new IllegalStateException("Primary stage not set. Call setStage() before attempting to switch scenes.");
        }
        
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(fxmlFile));
        Parent root = loader.load();
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}