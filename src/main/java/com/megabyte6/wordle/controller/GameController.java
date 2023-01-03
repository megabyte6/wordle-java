package com.megabyte6.wordle.controller;

import static com.megabyte6.wordle.util.Range.range;

import static javafx.util.Duration.millis;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.fxexperience.javafx.animation.ShakeTransition;
import com.megabyte6.wordle.App;
import com.megabyte6.wordle.model.Game;
import com.megabyte6.wordle.util.SceneManager;
import com.megabyte6.wordle.util.Stats;
import com.megabyte6.wordle.util.tuple.Pair;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class GameController extends Controller {

    private final String CORRECT_COLOR = "#1d8a0a";
    private final String WRONG_SPOT_COLOR = "#c9a908";
    private final String INCORRECT_COLOR = "#ad0c0c";
    private final CornerRadii CORNER_RADIUS = new CornerRadii(8);
    private final String[] WIN_MESSAGES = {
            "Genius",
            "Magnificent",
            "Impressive",
            "Splendid",
            "Great",
            "Phew"
    };

    private boolean uiDisabled = false;

    private Game game = new Game(this);

    @FXML
    private StackPane root;
    @FXML
    private GridPane gameBoard;
    @FXML
    private VBox keyboard;
    @FXML
    private Label popup;

    @Override
    public void initialize() {
        SceneManager.getScene().setOnKeyPressed(event -> typeKey(event.getCode()));
    }

    private void typeKey(KeyCode key) {
        if (game.isGameOver() || uiDisabled)
            return;

        switch (key) {
            case BACK_SPACE:
                if (game.cursorIsAtMinIndex())
                    break;

                game.decrementCursorIndex();
                game.setLetter("");
                break;

            case ENTER:
                if (!game.cursorIsAtMaxIndex()) {
                    inputNotValid("Not enough letters");
                    break;
                }
                if (!game.isWord(game.getCurrentGuess())) {
                    inputNotValid("Not in word list");
                    break;
                }

                checkGuess(() -> {
                    if (game.guessIsCorrect())
                        gameWon();
                    if (game.isOnLastGuess() && game.cursorIsAtMaxIndex())
                        gameLost();
                });

                if (!game.isOnLastGuess())
                    game.setCursorIndex(0);
                game.incrementGuessCount();

                break;

            default:
                if (game.cursorIsAtMaxIndex())
                    break;
                if (!key.isLetterKey())
                    break;

                game.setLetter(key.getChar());
                game.incrementCursorIndex();
        }

        // Restore focus to the scene otherwise the key presses will not be
        // detected.
        SceneManager.getScene().getRoot().requestFocus();
    }

    @FXML
    private void keyboardKeyPressed(ActionEvent event) {
        if (!(event.getSource() instanceof Button))
            return;
        Button button = (Button) event.getSource();
        String text = button.getText().toUpperCase();

        typeKey(switch (text) {
            case "ENTER" -> KeyCode.ENTER;
            case "⌫" -> KeyCode.BACK_SPACE;
            default -> KeyCode.valueOf(text);
        });
    }

    @FXML
    private void statsButtonPressed(ActionEvent event) {
        showStats(() -> {
        });
    }

    private void inputNotValid(String message) {
        getNodesByRow(game.getGuessCount()).stream()
                .forEach(node -> {
                    ShakeTransition shake = new ShakeTransition(node, millis(750), millis(0));
                    shake.play();
                });
        popup(message);
    }

    private void checkGuess(Runnable runAfter) {
        String word = game.getCurrentWord();
        String guess = game.getCurrentGuess();

        RotateTransition[] rotateTransitions = new RotateTransition[guess.length()];

        for (int i : range(guess.length())) {
            Label cell = (Label) getNodeByPosition(game.getGuessCount(), i);

            String color;
            if (guess.charAt(i) == word.charAt(i)) {
                color = CORRECT_COLOR;
            } else if (word.indexOf(guess.charAt(i)) != -1) {
                color = WRONG_SPOT_COLOR;
            } else {
                color = INCORRECT_COLOR;
            }

            setKeyboardKey(guess.charAt(i), color);

            final Background background = new Background(
                    new BackgroundFill(Color.web(color), CORNER_RADIUS, null));

            RotateTransition rotate2 = new RotateTransition(millis(200), cell);
            rotate2.setAxis(Rotate.X_AXIS);
            rotate2.setByAngle(-90);

            RotateTransition rotate1 = new RotateTransition(millis(200), cell);
            rotate1.setAxis(Rotate.X_AXIS);
            rotate1.setByAngle(90);
            rotate1.setOnFinished(event -> {
                cell.setBackground(background);
                rotate2.play();
            });

            rotateTransitions[i] = rotate1;
        }

        // Build animation.
        SequentialTransition sequentialTransition = new SequentialTransition();
        sequentialTransition.getChildren().addAll(rotateTransitions);
        sequentialTransition.setOnFinished(event -> {
            // Unlock keyboard.
            uiDisabled = false;

            runAfter.run();
        });

        // Disable keyboard.
        uiDisabled = true;

        // Perform animation.
        sequentialTransition.play();
    }

    private void setUIDisabled(boolean disabled) {
        keyboard.getChildren().forEach(hbox -> ((HBox) hbox).getChildren()
                .forEach(key -> ((Button) key).setDisable(disabled)));
        root.getChildren().get(0).setOpacity(disabled
                ? 0.5
                : 1);

        uiDisabled = disabled;
    }

    private void showStats(Runnable runOnClose) {
        setUIDisabled(true);

        final Pair<Node, Controller> pair = SceneManager.loadFXML("Stats.fxml");
        final Node content = pair.a();
        final StatsController controller = (StatsController) pair.b();
        root.getChildren().add(content);

        controller.initialize();
        controller.runOnClose(() -> {
            root.getChildren().remove(content);
            setUIDisabled(false);

            runOnClose.run();
        });
    }

    private void gameWon() {
        game.setGameOver(true);

        // Update stats.
        final Stats oldStats = App.stats;

        final int numberOfGamesPlayed = oldStats.numberOfGamesPlayed() + 1;
        final int numberOfWins = oldStats.numberOfWins() + 1;
        final int currentWinStreak = oldStats.currentWinStreak() + 1;
        final int longestWinStreak = oldStats.currentWinStreak() + 1 > oldStats.longestWinStreak()
                ? oldStats.currentWinStreak() + 1
                : oldStats.longestWinStreak();
        int[] guessDistribution = oldStats.guessDistribution();
        guessDistribution[game.getGuessCount()]++;

        App.stats = new Stats(
                numberOfGamesPlayed,
                numberOfWins,
                currentWinStreak,
                longestWinStreak,
                guessDistribution);

        // Show stats and reset the game.
        PauseTransition pauseTransition = new PauseTransition(millis(1500));
        pauseTransition.setOnFinished((event) -> {
            showStats(() -> {
                SceneManager.switchScenes("Game.fxml", Duration.millis(400));
            });
        });
        popup(WIN_MESSAGES[game.getGuessCount() - 1]);
        pauseTransition.play();
    }

    private void gameLost() {
        game.setGameOver(true);
        setUIDisabled(true);

        // Update stats.
        final Stats oldStats = App.stats;

        final int numberOfGamesPlayed = oldStats.numberOfGamesPlayed() + 1;
        final int numberOfWins = oldStats.numberOfWins();
        final int currentWinStreak = 0;
        final int longestWinStreak = oldStats.longestWinStreak();
        final int[] guessDistribution = oldStats.guessDistribution();

        App.stats = new Stats(
                numberOfGamesPlayed,
                numberOfWins,
                currentWinStreak,
                longestWinStreak,
                guessDistribution);

        final Pair<Node, Controller> pair = SceneManager.loadFXML("GameLost.fxml");
        final Node content = pair.a();
        final GameLostController controller = (GameLostController) pair.b();
        root.getChildren().add(content);

        controller.setCorrectWord(game.getCurrentWord());
        controller.runOnClose(() -> {
            root.getChildren().remove(content);
            setUIDisabled(false);

            // Show stats and reset the game.
            showStats(() -> {
                SceneManager.switchScenes("Game.fxml", Duration.millis(400));
            });
        });
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

    private List<Node> getKeyboardKeys() {
        List<Node> keys = new ArrayList<>();
        keyboard.getChildren().forEach(hBox -> {
            if (!(hBox instanceof HBox))
                return;
            ((HBox) hBox).getChildren().forEach(key -> keys.add(key));
        });
        return keys;
    }

    private void setKeyboardKey(char key, String color) {
        setKeyboardKey(Character.toString(key), color);
    }

    private void setKeyboardKey(String key, String color) {
        Button button;
        try {
            button = (Button) getKeyboardKeys().stream()
                    .filter(node -> node instanceof Button
                            && ((Button) node).getText().toLowerCase().equals(key.toLowerCase()))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            System.err.println("ERROR: No key with the value '" + key + "'");
            return;
        }
        // button.setBackground(color);
        button.setStyle("-fx-background-color: " + color + ";");
    }

    private void popup(String text) {
        popup(text, millis(1000), millis(250));
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
