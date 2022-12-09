package com.megabyte6.wordle;

import java.io.IOException;

import com.megabyte6.wordle.util.Data;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    public static final Color BACKGROUND_COLOR = Color.web("#0b2b37");

    public static Data data = new Data();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("view/Start.fxml")));

        primaryStage.setTitle("Wordle");
        primaryStage.setScene(scene);

        primaryStage.show();

        data.add("stage", primaryStage);
        data.add("scene", scene);
        data.addObserver("scene", (val) -> {
            Scene newScene = (Scene) val;
            Stage stage = (Stage) data.get("stage");
            if ((Boolean) data.get("fadeScenes"))
                newScene.getRoot().setOpacity(0);
            stage.setScene((Scene) newScene);
        });
        data.add("fadeScenes", true);
    }

    public static void switchScenes(String fxmlFileName, Duration totalTransitionDuration) {
        // Load new scene.
        Scene newScene;
        try {
            newScene = new Scene(FXMLLoader.load(App.class.getResource("view/" + fxmlFileName)));
        } catch (IOException e) {
            System.err.println("ERROR: Cannot read file");
            e.printStackTrace();
            return;
        }
        newScene.setFill(BACKGROUND_COLOR);
        Parent newRoot = newScene.getRoot();

        // Get old scene.
        Scene oldScene = (Scene) data.get("scene");
        Parent oldRoot = oldScene.getRoot();
        oldScene.setFill(BACKGROUND_COLOR);

        // Set up fade transitions.
        FadeTransition oldSceneFadeIn = new FadeTransition(totalTransitionDuration.divide(2), oldRoot);
        oldSceneFadeIn.setFromValue(1);
        oldSceneFadeIn.setToValue(0);

        FadeTransition newSceneFadeOut = new FadeTransition(totalTransitionDuration.divide(2), newRoot);
        newSceneFadeOut.setFromValue(0);
        newSceneFadeOut.setToValue(1);

        // Play transitions.
        oldSceneFadeIn.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                // Swap scenes between transitions.
                data.set("scene", newScene);
                // Start the new scene's fade-in animation.
                newSceneFadeOut.play();
            };
        });
        oldSceneFadeIn.play();
    }

}
