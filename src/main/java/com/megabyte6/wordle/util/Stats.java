package com.megabyte6.wordle.util;

public record Stats(int numberOfGamesPlayed, int numberOfWins, int currentWinStreak, int longestWinStreak,
        int[] guessDistribution) {
}
