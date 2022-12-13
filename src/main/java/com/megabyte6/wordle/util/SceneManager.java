package com.megabyte6.wordle.util;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        Parent oldRoot = oldScene.getRoot();
        oldScene.setFill(SceneManager.backgroundColor);

        // Load new scene.
        final String path = "/com/megabyte6/wordle/view/" + fxmlFileName;
        Scene newScene;
        try {
            newScene = new Scene(FXMLLoader.load(SceneManager.class.getResource(path)));
        } catch (IOException e) {
            System.err.println("ERROR: Cannot read file '" + path + "'");
            e.printStackTrace();
            return;
        }
        newScene.setFill(SceneManager.backgroundColor);
        newScene.getRoot().setOpacity(0);
        Parent newRoot = newScene.getRoot();

        // Set the current scene to the new scene.
        SceneManager.scene = newScene;

        // Set up fade transitions.
        FadeTransition oldSceneFadeOut = new FadeTransition(totalTransitionDuration.divide(2), oldRoot);
        oldSceneFadeOut.setFromValue(1);
        oldSceneFadeOut.setToValue(0);

        FadeTransition newSceneFadeIn = new FadeTransition(totalTransitionDuration.divide(2), newRoot);
        newSceneFadeIn.setFromValue(0);
        newSceneFadeIn.setToValue(1);

        // Play transitions.
        oldSceneFadeOut.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // Swap scenes between transitions.
                SceneManager.stage.setScene(newScene);
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
