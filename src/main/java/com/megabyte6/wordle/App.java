package com.megabyte6.wordle;

import com.megabyte6.wordle.util.SceneManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    public static final Color BACKGROUND_COLOR = Color.web("#0b2b37");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("view/Start.fxml")));

        primaryStage.setTitle("Wordle");
        primaryStage.setScene(scene);
        primaryStage.show();

        SceneManager.init(primaryStage, scene, BACKGROUND_COLOR);
    }

}
