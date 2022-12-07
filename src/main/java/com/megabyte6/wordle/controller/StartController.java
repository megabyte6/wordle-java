package com.megabyte6.wordle.controller;

import com.megabyte6.wordle.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class StartController {

    @FXML
    public VBox root;

    @FXML
    public Button start;

    public void start() {
        App.switchScenes("Game.fxml");
    }

}
