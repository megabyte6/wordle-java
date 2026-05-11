package com.megabyte6.wordle.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class WordManager {

    private final List<String> dictionary;
    private List<String> words;
    private List<String> usedWords;

    public WordManager() {
        URL dictionaryResource = WordManager.class.getResource("/com/megabyte6/wordle/dictionary.txt");
        dictionary = readFile(dictionaryResource).stream()
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(ArrayList::new));

        URL wordsResource = WordManager.class.getResource("/com/megabyte6/wordle/wordbank.txt");
        words = readFile(wordsResource).stream()
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(LinkedList::new));

        usedWords = new LinkedList<>();
    }

    private List<String> readFile(URL url) {
        List<String> content = new LinkedList<>();
        if (url == null) {
            System.err.println("ERROR: resource URL is null");
            return content;
        }

        try (InputStream in = url.openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            content = reader.lines().collect(Collectors.toCollection(LinkedList::new));
        } catch (IOException e) {
            System.err.println("ERROR: failed to read resource");
            e.printStackTrace();
        }

        return content;
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

    public boolean isWord(String word) {
        if (dictionary.contains(word))
            return true;
        return false;
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
