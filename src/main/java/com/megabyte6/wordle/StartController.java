package com.megabyte6.wordle;

import java.io.IOException;

import com.megabyte6.wordle.exception.KeyDoesNotExistException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartController {

    @FXML
    public VBox root;

    @FXML
    public Button start;

    public void start() throws IOException, KeyDoesNotExistException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Game.fxml")));
        // Stage primaryStage = (Stage) root.getScene().getWindow();
        // primaryStage.setScene(scene);
        App.data.set("scene", scene);
    }

}
