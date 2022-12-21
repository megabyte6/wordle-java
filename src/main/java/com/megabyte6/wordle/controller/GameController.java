package com.megabyte6.wordle.controller;

import static com.megabyte6.wordle.util.Range.range;

import static javafx.util.Duration.millis;

import java.util.List;
import java.util.stream.Collectors;

import com.fxexperience.javafx.animation.ShakeTransition;
import com.megabyte6.wordle.model.Game;
import com.megabyte6.wordle.util.SceneManager;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class GameController implements Controller {

    private final Color CORRECT_COLOR = Color.web("#538d4e");
    private final Color WRONG_SPOT_COLOR = Color.web("#b59f3b");
    private final Color INCORRECT_COLOR = Color.web("#864d47");
    private final CornerRadii CORNER_RADIUS = new CornerRadii(8);

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
                // Show which letters are correct.
                checkGuess();
                // Guess is correct.
                if (game.guessIsCorrect()) {
                    winGame();
                    break;
                }
                game.incrementAttemptCount();
                game.setCursorIndex(0);
                break;

            default:
                // Cursor is at the end of the line.
                if (game.cursorIsAtMaxIndex())
                    break;
                // The key pressed isn't a letter.
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

    private void checkGuess() {
        String word = game.getCurrentWord();
        String guess = game.getCurrentGuess();

        RotateTransition[] rotateTransitions = new RotateTransition[guess.length()];

        for (int i : range(guess.length())) {
            Label cell = (Label) getNodeByPosition(game.getAttemptNum(), i);
            BackgroundFill backgroundFill = new BackgroundFill(INCORRECT_COLOR, CORNER_RADIUS, null);

            if (guess.charAt(i) == word.charAt(i)) {
                backgroundFill = new BackgroundFill(CORRECT_COLOR, CORNER_RADIUS, null);
            } else if (word.indexOf(guess.charAt(i)) != -1) {
                backgroundFill = new BackgroundFill(WRONG_SPOT_COLOR, CORNER_RADIUS, null);
            }

            final Background background = new Background(backgroundFill);

            RotateTransition rotate2 = new RotateTransition(millis(250), cell);
            rotate2.setAxis(Rotate.X_AXIS);
            rotate2.setByAngle(-90);

            RotateTransition rotate1 = new RotateTransition(millis(250), cell);
            rotate1.setAxis(Rotate.X_AXIS);
            rotate1.setByAngle(90);
            rotate1.setOnFinished((event) -> {
                cell.setBackground(background);
                rotate2.play();
            });

            rotateTransitions[i] = rotate1;
        }

        // Build animation.
        SequentialTransition sequentialTransition = new SequentialTransition();
        for (RotateTransition rotateTransition : rotateTransitions) {
            sequentialTransition.getChildren().add(rotateTransition);
            sequentialTransition.getChildren().add(new PauseTransition(millis(50)));
        }

        // Perform animation.
        sequentialTransition.play();
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
