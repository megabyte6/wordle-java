package com.megabyte6.wordle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static Data data = new Data();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Start.fxml"));

        Scene scene = new Scene(root);

        App.data.add("stage", primaryStage);
        App.data.add("scene", scene);

        primaryStage.setTitle("Wordle");
        primaryStage.setScene((Scene) App.data.get("scene"));

        primaryStage.show();
    }

}
