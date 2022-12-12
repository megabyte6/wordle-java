package com.megabyte6.wordle.model;

import java.util.UUID;

import com.megabyte6.wordle.App;

import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class Game {

    public static final UUID game;
    private static Game instance;

    private char[][] gameBoard = new char[6][5];
    private int numberOfAttempts = 0;

    private Game() {
    }

    static {
        instance = new Game();
        game = App.data.set(instance);
    }

    public static Game getInstance() {
        return instance;
    }

    public void init() {
        App.switchScenes("Game.fxml", Duration.millis(500));
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
