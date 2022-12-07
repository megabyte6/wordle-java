package com.megabyte6.wordle;

import java.io.IOException;

import com.megabyte6.wordle.util.Data;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

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
        data.addObserver("scene", (newScene) -> {
            Stage stage = (Stage) data.get("stage");
            stage.setScene((Scene) newScene);
        });
    }

    public static void switchScenes(String fxmlPath) {
        Scene scene;
        try {
            scene = new Scene(FXMLLoader.load(App.class.getResource("view/" + fxmlPath)));
        } catch (IOException e) {
            System.err.println("ERROR: Cannot read file");
            e.printStackTrace();
            return;
        }

        switchScenes(scene);
    }

    public static void switchScenes(Scene scene) {
        data.set("scene", scene);
    }

}
