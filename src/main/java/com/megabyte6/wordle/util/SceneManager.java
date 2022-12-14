package com.megabyte6.wordle.util;

import java.io.IOException;

import com.megabyte6.wordle.controller.Controller;
import com.megabyte6.wordle.util.tuple.Pair;
import com.megabyte6.wordle.util.tuple.Tuple;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneManager {

    private static final String RESOURCE_PATH = "/com/megabyte6/wordle/view/";

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
        final Scene oldScene = SceneManager.scene;
        oldScene.setFill(SceneManager.backgroundColor);

        // Load new scene.
        final String path = RESOURCE_PATH + fxmlFileName;
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource(path));

            SceneManager.scene = new Scene(fxmlLoader.load());
            SceneManager.scene.setFill(SceneManager.backgroundColor);
            SceneManager.scene.getRoot().setOpacity(0);

            Controller controller = fxmlLoader.getController();
            controller.initialize();
        } catch (IOException e) {
            System.err.println("ERROR: Cannot read file '" + path + "'");
            e.printStackTrace();
            return;
        }

        // Set up fade transitions.
        final FadeTransition oldSceneFadeOut = new FadeTransition(
                totalTransitionDuration.divide(2), oldScene.getRoot());
        oldSceneFadeOut.setFromValue(1);
        oldSceneFadeOut.setToValue(0);

        final FadeTransition newSceneFadeIn = new FadeTransition(
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

    public static Pair<Node, Controller> loadFXML(String fxmlFileName) {
        Node node;
        Controller controller;

        final String path = RESOURCE_PATH + fxmlFileName;
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource(path));
            node = fxmlLoader.load();
            controller = fxmlLoader.getController();
        } catch (IOException e) {
            System.err.println("ERROR: Cannot read file '" + path + "'");
            e.printStackTrace();
            return Tuple.of(null, null);
        }
        return Tuple.of(node, controller);
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
