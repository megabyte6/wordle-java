package com.megabyte6.wordle.model;

import com.megabyte6.wordle.App;

import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class Game {

    private char[][] gameBoard = new char[6][5];
    private int numberOfAttempts = 0;

    public Game() {
        App.data.add("game", this);
        App.switchScenes("Game.fxml", Duration.millis(500));
    }

    public void typeKey(KeyCode key) {
        if (key == KeyCode.BACK_SPACE)
            backspace();
    }

    public void backspace() {
    }

    public void checkWord() {
    }

}
