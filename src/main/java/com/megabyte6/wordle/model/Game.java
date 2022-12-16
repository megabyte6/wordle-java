package com.megabyte6.wordle.model;

import com.megabyte6.wordle.controller.GameController;
import com.megabyte6.wordle.util.WordGenerator;

public class Game {

    private GameController controller;
    private WordGenerator wordGenerator = new WordGenerator();

    private char[][] gameBoard = new char[6][5];
    private int attemptNum = 0, cursorIndex = 0;
    private String currentWord;

    public Game(GameController controller) {
        this.controller = controller;
        currentWord = wordGenerator.generate();
    }

    public boolean checkWord() {
        if (!getCurrentGuess().equals(currentWord))
            return false;
        cursorIndex = 0;
        return true;
    }

    public char[][] getGameBoard() {
        return this.gameBoard;
    }

    public void setGameBoard(int row, int column, char value) {
        if (row < 0 || column < 0)
            return;
        if (row >= gameBoard.length || column >= gameBoard[row].length)
            return;
        gameBoard[row][column] = value;
    }

    public int getAttemptNum() {
        return attemptNum;
    }

    public void setAttemptNum(int value) {
        if (value > gameBoard.length)
            return;
        attemptNum = value;
    }

    public void incrementCurrentAttemptCount() {
        setAttemptNum(attemptNum + 1);
    }

    public int getCursorIndex() {
        return cursorIndex;
    }

    public boolean cursorIsAtMinIndex() {
        return cursorIndex == 0;
    }

    public boolean cursorIsAtMaxIndex() {
        return cursorIndex == gameBoard[attemptNum].length;
    }

    public void setCursorIndex(int value) {
        if (value > gameBoard[attemptNum].length)
            return;
        cursorIndex = value;
    }

    public void incrementCursorIndex() {
        setCursorIndex(cursorIndex + 1);
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void generateNewWord() {
        currentWord = wordGenerator.generate();
    }

    public String getCurrentGuess() {
        return new String(gameBoard[attemptNum]);
    }

}
