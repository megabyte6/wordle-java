package com.megabyte6.wordle.controller;

import com.megabyte6.wordle.util.SceneManager;
import com.megabyte6.wordle.util.tuple.Pair;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class StartController extends Controller {

    @FXML
    private StackPane root;
    @FXML
    private Button startButton;

    @FXML
    private void start() {
        SceneManager.switchScenes("Game.fxml", Duration.millis(400));
    }

    @FXML
    private void statsButtonPressed(ActionEvent event) {
        showStats();
    }

    private void showStats() {
        setUIDisabled(true);

        final Pair<Node, Controller> pair = SceneManager.loadFXML("Stats.fxml");
        final Node content = pair.a();
        final StatsController controller = (StatsController) pair.b();
        root.getChildren().add(content);

        controller.initialize();
        controller.runOnClose(() -> {
            root.getChildren().remove(content);
            setUIDisabled(false);
        });
    }

    private void setUIDisabled(boolean disabled) {
        startButton.setDisable(disabled);
        root.getChildren().get(0).setOpacity(disabled
                ? 0.5
                : 1);
    }

}
