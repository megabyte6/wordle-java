package com.megabyte6.wordle.controller;

import static javafx.util.Duration.millis;

import java.util.List;
import java.util.stream.Collectors;

import com.fxexperience.javafx.animation.ShakeTransition;
import com.megabyte6.wordle.model.Game;
import com.megabyte6.wordle.util.SceneManager;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class GameController implements Controller {

    private Game game = new Game(this);

    @FXML
    private StackPane root;
    @FXML
    private GridPane gameBoard;

    @FXML
    private Label popup;

    @Override
    public void initListeners() {
        SceneManager.getScene().setOnKeyPressed(event -> typeKey(event.getCode()));
    }

    private void typeKey(KeyCode key) {
        switch (key) {
            case BACK_SPACE:
                // Nothing to delete
                if (game.cursorIsAtMinIndex())
                    break;
                game.decrementCursorIndex();
                game.setLetter("");
                break;

            case ENTER:
                // Cursor isn't at the end of the line.
                if (!game.cursorIsAtMaxIndex())
                    break;
                // Input isn't a word.
                if (!game.isWord(game.getCurrentGuess())) {
                    inputNotValid();
                    break;
                }
                // Guess is correct.
                if (game.checkGuess()) {
                    game.setCursorIndex(0);
                    winGame();
                    break;
                }
                break;

            default:
                if (game.cursorIsAtMaxIndex())
                    break;
                if (!key.isLetterKey())
                    break;

                game.setLetter(key.getChar());
                game.incrementCursorIndex();
        }
    }

    private void inputNotValid() {
        getNodesByRow(game.getAttemptNum()).stream()
                .forEach(node -> {
                    ShakeTransition shake = new ShakeTransition(node, millis(750), millis(0));
                    shake.play();
                });
        popup("Not in word list");
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
        Integer rowValue = row == 0
                ? null
                : row;
        Integer columnValue = column == 0
                ? null
                : column;
        return gameBoard.getChildren().stream()
                .filter(node -> GridPane.getRowIndex(node) == rowValue
                        && GridPane.getColumnIndex(node) == columnValue)
                .findFirst()
                .get();
    }

    private List<Node> getNodesByRow(int row) {
        // Check if row or column is 0 because GridPane.getRowIndex() and
        // GridPane.getColumnIndex() returns null for any indexes of 0.
        Integer rowValue = row == 0
                ? null
                : row;
        return gameBoard.getChildren().stream()
                .filter(node -> GridPane.getRowIndex(node) == rowValue)
                .collect(Collectors.toList());
    }

    private void popup(String text) {
        popup(text, millis(750), millis(200));
    }

    private void popup(String text, Duration pauseDuration, Duration transitionDuration) {
        popup.setText(text);

        final FadeTransition fadeIn = new FadeTransition();
        fadeIn.setNode(popup);
        fadeIn.setDuration(transitionDuration);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        final PauseTransition pause = new PauseTransition();
        pause.setDuration(pauseDuration);

        final FadeTransition fadeOut = new FadeTransition();
        fadeOut.setNode(popup);
        fadeOut.setDuration(transitionDuration);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        final SequentialTransition transition = new SequentialTransition();
        transition.getChildren().addAll(fadeIn, pause, fadeOut);

        transition.play();
    }

}
