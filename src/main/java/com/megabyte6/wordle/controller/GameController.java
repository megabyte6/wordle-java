package com.megabyte6.wordle.controller;

import com.megabyte6.wordle.App;
import com.megabyte6.wordle.model.Game;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class GameController {

    private Game game = (Game) App.data.get("game");

    @FXML
    private VBox root;
    @FXML
    private GridPane gameBoard;

    @FXML
    private void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER:
                game.checkWord();
                break;
            default:
                game.typeKey(event.getCode());
                break;
        }
    }

    public void setBox(String text, int row, int column) {
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
