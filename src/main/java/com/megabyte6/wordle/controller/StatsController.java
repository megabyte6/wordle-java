package com.megabyte6.wordle.controller;

import static com.megabyte6.wordle.util.Range.range;

import com.megabyte6.wordle.App;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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
            final int winCount = App.stats.guessDistribution()[i];
            if (winCount == 0)
                continue;

            final Data data = new Data(i + " attempt", winCount);
            // Check if words should be plural.
            if (i > 1)
                data.setName(data.getName() + "s");

            guessDistributionData.add(data);
        }
        guessDistribution.setData(guessDistributionData);

        // Add tooltips to show on hover for the pie chart.
        guessDistribution.getData().forEach(data -> {
            final String guessCount = data.getName().substring(0, 1);
            final int winCount = App.stats.guessDistribution()[Integer.valueOf(guessCount)];
            final Tooltip tooltip = new Tooltip(winCount + " win");
            tooltip.setStyle("-fx-font-size: 14");
            // Check if words should be plural.
            if (winCount > 1)
                tooltip.setText(tooltip.getText() + "s");

            Tooltip.install(data.getNode(), tooltip);
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
