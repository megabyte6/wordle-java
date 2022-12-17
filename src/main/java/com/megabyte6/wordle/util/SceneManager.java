package com.megabyte6.wordle.util;

import java.io.IOException;

import com.megabyte6.wordle.controller.Controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneManager {

    private static Stage stage;
    private static Scene scene;
    private static Color backgroundColor;

    private SceneManager() {
    }

    public static void init(Stage stage, Scene scene) {
        init(stage, scene, Color.WHITE);
    }

    public static void init(Stage stage, Scene scene, Color backgroundColor) {
        SceneManager.stage = stage;
        SceneManager.scene = scene;
        SceneManager.backgroundColor = backgroundColor;
    }

    public static void switchScenes(String fxmlFileName, Duration totalTransitionDuration) {
        // Get old scene.
        Scene oldScene = SceneManager.scene;
        oldScene.setFill(SceneManager.backgroundColor);

        // Load new scene.
        final String path = "/com/megabyte6/wordle/view/" + fxmlFileName;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource(path));
            // Set the current scene to the new scene.
            SceneManager.scene = new Scene(fxmlLoader.load());
            SceneManager.scene.setFill(SceneManager.backgroundColor);
            SceneManager.scene.getRoot().setOpacity(0);
            // Initialize listeners.
            Controller controller = fxmlLoader.getController();
            controller.initListeners();
        } catch (IOException e) {
            System.err.println("ERROR: Cannot read file '" + path + "'");
            e.printStackTrace();
            return;
        }

        // Set up fade transitions.
        FadeTransition oldSceneFadeOut = new FadeTransition(
                totalTransitionDuration.divide(2), oldScene.getRoot());
        oldSceneFadeOut.setFromValue(1);
        oldSceneFadeOut.setToValue(0);

        FadeTransition newSceneFadeIn = new FadeTransition(
                totalTransitionDuration.divide(2), SceneManager.scene.getRoot());
        newSceneFadeIn.setFromValue(0);
        newSceneFadeIn.setToValue(1);

        // Play transitions.
        oldSceneFadeOut.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // Swap scenes between transitions.
                SceneManager.stage.setScene(SceneManager.scene);
                // Start the new scene's fade-in animation.
                newSceneFadeIn.play();
            };
        });
        oldSceneFadeOut.play();
    }

    public static Stage getStage() {
        return SceneManager.stage;
    }

    public static Scene getScene() {
        return SceneManager.scene;
    }

    public static Color getBackgroundColor() {
        return SceneManager.backgroundColor;
    }

    public static void setBackgroundColor(Color color) {
        SceneManager.backgroundColor = color;
    }

}
