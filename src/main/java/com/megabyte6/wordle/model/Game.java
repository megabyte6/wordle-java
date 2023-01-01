package com.megabyte6.wordle.model;

import com.megabyte6.wordle.controller.GameController;
import com.megabyte6.wordle.util.WordManager;

public class Game {

    private GameController controller;
    private WordManager wordManager = new WordManager();

    private char[][] gameBoard = new char[6][5];
    private int guessCount = 0, cursorIndex = 0;
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
        setGameBoard(guessCount, cursorIndex, value);
    }

    public boolean isWord(String word) {
        return wordManager.isWord(word.toLowerCase());
    }

    public boolean guessIsCorrect() {
        if (!isWord(getCurrentGuess()))
            return false;
        if (!getCurrentGuess().equals(currentWord))
            return false;
        return true;
    }

    public String getCurrentGuess() {
        return new String(gameBoard[guessCount]).toLowerCase();
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

    public int getGuessCount() {
        return guessCount;
    }

    public boolean isOnLastGuess() {
        return guessCount == gameBoard.length - 1;
    }

    public void setGuessCount(int value) {
        if (value < 0 || value >= gameBoard.length)
            return;
        guessCount = value;
    }

    public void incrementGuessCount() {
        setGuessCount(guessCount + 1);
    }

    public int getCursorIndex() {
        return cursorIndex;
    }

    public boolean cursorIsAtMinIndex() {
        return cursorIndex == 0;
    }

    public boolean cursorIsAtMaxIndex() {
        return cursorIndex == gameBoard[guessCount].length;
    }

    public void setCursorIndex(int value) {
        if (value < 0 || value > gameBoard[guessCount].length)
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
