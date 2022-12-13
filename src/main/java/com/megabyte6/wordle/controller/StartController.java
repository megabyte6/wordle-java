package com.megabyte6.wordle.controller;

import com.megabyte6.wordle.model.Game;

import javafx.fxml.FXML;

public class StartController {

    @FXML
    private void start() {
        Game.getInstance().init();
    }

}
