package com.megabyte6.wordle.model;

import com.megabyte6.wordle.controller.GameController;
import com.megabyte6.wordle.util.WordManager;

public class Game {

    private GameController controller;
    private WordManager wordManager = new WordManager();

    private char[][] gameBoard = new char[6][5];
    private int attemptNum = 0, cursorIndex = 0;
    private String currentWord;

    public Game(GameController controller) {
        this.controller = controller;
        currentWord = wordManager.generate();
    }

    public void setLetter(String value) {
        if (value.length() > 0) {
            setLetter(value.charAt(0));
        } else {
            setLetter('\u0000');
        }
    }

    public void setLetter(char value) {
        if (cursorIsAtMaxIndex())
            return;
        setGameBoard(attemptNum, cursorIndex, value);
    }

    public boolean isWord(String word) {
        return wordManager.contains(word);
    }

    public boolean checkGuess() {
        if (!isWord(getCurrentGuess()))
            return false;
        if (!getCurrentGuess().equals(currentWord))
            return false;
        setCursorIndex(0);
        return true;
    }

    public String getCurrentGuess() {
        return new String(gameBoard[attemptNum]);
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
        String text = Character.toString(value).toUpperCase();
        controller.setBoxText(text, row, column);
    }

    public int getAttemptNum() {
        return attemptNum;
    }

    public void setAttemptNum(int value) {
        if (value < 0 || value > gameBoard.length)
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
        if (value < 0 || value > gameBoard[attemptNum].length)
            return;
        cursorIndex = value;
    }

    public void incrementCursorIndex() {
        setCursorIndex(cursorIndex + 1);
    }

    public void decrementCursorIndex() {
        setCursorIndex(cursorIndex - 1);
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void generateNewWord() {
        currentWord = wordManager.generate();
    }

}
