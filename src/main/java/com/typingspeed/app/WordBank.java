package com.typingspeed.app;

import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Random;

/** @author Ethan Rocklin */

public class WordBank {

    private static final List<String> BANK = loadWords();
    private static final int BANK_SIZE = BANK.size();
    
    public WordBank() {}
    
    /**
     * This method generates and returns a list of words
     * from atomic_words.txt.
     *  @param count : the number of words to include
     *  @return a String of words, each separated by a space character.
     */
    public static String getWords(int count) {
        StringBuilder words = new StringBuilder();
        Random rand = new Random();
        // Populates the List with random words from the "bank" field.
        for (int i = 0; i < count; i++) {
            words.append(BANK.get(rand.nextInt(BANK_SIZE)) + " ");
        }
        return words.toString();
    }
    
    // This method loads the words into memory upon class instantiation
    private static List<String> loadWords() {
        List<String> words = null;
        // Loads words into the "bank" field.
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(WordBank.class.getResourceAsStream("/words/atomic_words.txt")))) {
            words = reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
        }
        return words;
    }
    
}
