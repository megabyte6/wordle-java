package com.megabyte6.wordle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameLostController extends Controller {

    private Runnable runOnClose = () -> {
    };

    @FXML
    private Label correctWord;

    @FXML
    private void close() {
        runOnClose.run();
    }

    public void setCorrectWord(String word) {
        correctWord.setText(word.toUpperCase());
    }

    public void runOnClose(Runnable runOnClose) {
        this.runOnClose = runOnClose;
    }

}
