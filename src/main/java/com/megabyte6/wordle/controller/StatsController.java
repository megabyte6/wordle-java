package com.megabyte6.wordle.controller;

import com.megabyte6.wordle.model.Game;

import javafx.fxml.FXML;

public class StatsController extends Controller {

    private Game game;

    private Runnable runOnClose = () -> {
    };

    @Override
    public void initialize() {
        if (game == null)
            return;
    }

    @FXML
    private void close() {
        runOnClose.run();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void runOnClose(Runnable runOnClose) {
        this.runOnClose = runOnClose;
    }

}
