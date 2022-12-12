package com.megabyte6.wordle.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WordGenerator {

    private LinkedList<String> words;
    private LinkedList<String> usedWords;

    public WordGenerator() {
        words = readFile(new File("wordlist.txt"));
        usedWords = new LinkedList<>();
    }

    private LinkedList<String> readFile(File file) {
        return readFile(file, false);
    }

    private LinkedList<String> readFile(File file, boolean printContent) {
        LinkedList<String> content = new LinkedList<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));

            String line = "";
            while ((line = reader.readLine()) != null) {
                content.add(line);

                if (printContent)
                    System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: '" + file.getPath() + "' not found.");
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("ERROR: failed to read '" + file.getPath() + "'.");
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
