package com.megabyte6.wordle.controller;

import static com.megabyte6.wordle.util.Range.range;

import com.megabyte6.wordle.App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class StatsController extends Controller {

    private Runnable runOnClose = () -> {
    };

    @FXML
    private VBox root;
    @FXML
    private Label gamesPlayed;
    @FXML
    private Label winPercentage;
    @FXML
    private Label currentStreak;
    @FXML
    private Label longestStreak;
    @FXML
    private PieChart guessDistribution;

    @Override
    public void initialize() {
        if (App.stats == null)
            return;
        gamesPlayed.setText(App.stats.numberOfGamesPlayed() + "");
        winPercentage.setText(App.stats.winPercentage() + "");
        currentStreak.setText(App.stats.currentWinStreak() + "");
        longestStreak.setText(App.stats.longestWinStreak() + "");

        ObservableList<Data> guessDistributionData = FXCollections.observableArrayList();
        for (var i : range(App.stats.guessDistribution().length)) {
            int guessCount = App.stats.guessDistribution()[i];
            if (guessCount == 0)
                continue;
            guessDistributionData.add(new Data(Integer.toString(i), guessCount));
        }
        guessDistribution.setData(guessDistributionData);

        final Label caption = new Label();
        root.getChildren().add(caption);
        guessDistribution.getData().stream()
                .forEach(data -> {
                    data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                        caption.setTranslateX(event.getSceneX());
                        caption.setTranslateY(event.getSceneY());
                        caption.setText("# of guesses: " + data.getName() + "\n"
                                + "# of wins: " + data.getPieValue());
                    });
                });
    }

    @FXML
    private void close() {
        runOnClose.run();
    }

    public void runOnClose(Runnable runOnClose) {
        this.runOnClose = runOnClose;
    }

}
