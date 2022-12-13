package com.megabyte6.wordle.model;

import com.megabyte6.wordle.util.SceneManager;

import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class Game {

    private static final Game instance;

    private char[][] gameBoard = new char[6][5];
    private int numberOfAttempts = 0;

    static {
        instance = new Game();
    }

    private Game() {
    }

    public static Game getInstance() {
        return instance;
    }

    public void init() {
        SceneManager.switchScenes("Game.fxml", Duration.millis(500));
    }

    public void typeKey(KeyCode key) {
        if (key == KeyCode.BACK_SPACE) {
            backspace();
            return;
        }
        System.out.println("'" + gameBoard[0][0] + "'");
    }

    public void backspace() {
    }

    public void checkWord() {
    }

}
