package com.megabyte6.wordle.controller;

import com.megabyte6.wordle.util.SceneManager;

import javafx.fxml.FXML;
import javafx.util.Duration;

public class StartController extends Controller {

    @FXML
    private void start() {
        SceneManager.switchScenes("Game.fxml", Duration.millis(400));
    }

}
