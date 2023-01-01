package com.megabyte6.wordle.controller;

import com.megabyte6.wordle.App;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatsController extends Controller {

    private Runnable runOnClose = () -> {
    };

    @FXML
    private Label gamesPlayed;
    @FXML
    private Label winPercentage;
    @FXML
    private Label currentStreak;
    @FXML
    private Label longestStreak;

    @Override
    public void initialize() {
        if (App.stats == null)
            return;
        gamesPlayed.setText(App.stats.numberOfGamesPlayed() + "");
        winPercentage.setText(App.stats.numberOfWins() / App.stats.numberOfGamesPlayed() * 100 + "%");
        currentStreak.setText(App.stats.currentWinStreak() + "");
        longestStreak.setText(App.stats.longestWinStreak() + "");
    }

    @FXML
    private void close() {
        runOnClose.run();
    }

    public void runOnClose(Runnable runOnClose) {
        this.runOnClose = runOnClose;
    }

}
