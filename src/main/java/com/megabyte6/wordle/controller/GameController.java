package com.megabyte6.wordle.controller;

import com.megabyte6.wordle.model.Game;
import com.megabyte6.wordle.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class GameController implements Controller {

    private Game game = new Game(this);

    @FXML
    private StackPane root;
    @FXML
    private GridPane gameBoard;

    @Override
    public void initListeners() {
        SceneManager.getScene().setOnKeyPressed(event -> typeKey(event.getCode()));
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

        game.setLetter(key.getChar());
        game.incrementCursorIndex();
    }

    private void backspace() {
        if (game.cursorIsAtMinIndex())
            return;
        game.decrementCursorIndex();
        game.setLetter("");
    }

    private void winGame() {
    }

    public void setBoxText(String text, int row, int column) {
        Label label = (Label) getNodeByPosition(row, column);
        label.setText(text);
    }

    private Node getNodeByPosition(int row, int column) {
        // Check if row or column is 0 because GridPane.getRowIndex() and
        // GridPane.getColumnIndex() returns null for any indexes of 0.
        Integer rowValue = row == 0 ? null : row;
        Integer columnValue = column == 0 ? null : column;
        return gameBoard.getChildren().stream()
                .filter(node -> GridPane.getRowIndex(node) == rowValue
                        && GridPane.getColumnIndex(node) == columnValue)
                .findFirst()
                .get();
    }

}
