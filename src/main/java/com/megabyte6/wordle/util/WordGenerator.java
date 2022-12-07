package com.megabyte6.wordle.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WordGenerator {

    private LinkedList<String> words = new LinkedList<>();
    private LinkedList<String> usedWords = new LinkedList<>();

    public WordGenerator() {
        getWordsFromFile();
    }

    private void getWordsFromFile() {
        // TODO: Open file and get words.
    }

    public String generate() {
        // Recycle all of the words once the user has exhausted all of them.
        if (words.isEmpty()) {
            words = new LinkedList<>(usedWords);
            usedWords.clear();
        }

        Random rand = new Random();

        int index;
        String word;
        // Use while loop to check for duplicates.
        do {
            index = rand.nextInt(words.size());
            word = words.get(index);
            words.remove(index);
        } while (usedWords.contains(word));
        usedWords.add(word);

        return word;
    }

    public String[] getWords() {
        return (String[]) words.toArray();
    }

    public void setWords(List<String> words) {
        if (words == null)
            return;
        this.words = new LinkedList<>(words);
    }

    public void addWord(String word) {
        words.add(word);
    }

    public boolean removeWord(String word) {
        return words.remove(word);
    }

    public String removeWord(int index) {
        return words.remove(index);
    }

    public String[] getUsedWords() {
        return (String[]) usedWords.toArray();
    }

    public void setUsedWords(List<String> usedWords) {
        if (words == null)
            return;
        this.usedWords = new LinkedList<>(usedWords);
    }

    public void addUsedWord(String usedWord) {
        usedWords.add(usedWord);
    }

    public boolean removeUsedWord(String usedWord) {
        return usedWords.remove(usedWord);
    }

    public String removeUsedWord(int index) {
        return usedWords.remove(index);
    }

}
