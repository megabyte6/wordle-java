package com.megabyte6.wordle.controller;

import java.io.IOException;

import com.megabyte6.wordle.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class StartController {

    @FXML
    public VBox root;

    @FXML
    public Button start;

    public void start() throws IOException {
        App.switchScenes("Game.fxml");
    }

}
