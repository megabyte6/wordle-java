package com.megabyte6.wordle.controller;

import com.megabyte6.wordle.model.Game;
import com.megabyte6.wordle.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class GameController {

    private Game game = new Game(this);

    @FXML
    private StackPane root;
    @FXML
    private GridPane gameBoard;

    @FXML
    private void initialize() {
        SceneManager.getScene().setOnKeyPressed((KeyEvent event) -> typeKey(event.getCode()));
    }

    private void typeKey(KeyCode key) {
        if (key == KeyCode.BACK_SPACE) {
            backspace();
            return;
        }
        if (key == KeyCode.ENTER) {
            if (game.checkWord())
                winGame();
            return;
        }
        if (game.cursorIsAtMaxIndex())
            return;
        game.incrementCursorIndex();
    }

    private void backspace() {
        if (game.cursorIsAtMinIndex())
            return;
    }

    private void winGame() {
    }

    private void setBoxText(String text, int row, int column) {
        Label label = (Label) getNodeByPosition(row, column);
        label.setText(text);
    }

    private Node getNodeByPosition(int row, int column) {
        return gameBoard.getChildren().stream()
                .filter(node -> GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column)
                .findFirst()
                .get();
    }

}
