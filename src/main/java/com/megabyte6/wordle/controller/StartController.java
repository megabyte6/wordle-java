package com.megabyte6.wordle.controller;

import com.megabyte6.wordle.model.Game;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

public class StartController {

    @FXML
    private void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER:
                start();
                break;
            default:
                break;
        }
    }

    @FXML
    private void start() {
        new Game();
    }

}
