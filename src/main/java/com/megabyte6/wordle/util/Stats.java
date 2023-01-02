package com.megabyte6.wordle.util;

import java.io.Serializable;

public record Stats(int numberOfGamesPlayed, int numberOfWins, int currentWinStreak, int longestWinStreak,
        int[] guessDistribution) implements Serializable {

    public int winPercentage() {
        return (int) (100.0 * numberOfWins / numberOfGamesPlayed);
    }

}
