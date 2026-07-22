package com.typingspeed.app;

import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Random;

/**
 * 
 * @author Ethan Rocklin
 */

public class WordBank {

    private static List<String> bank;
    private static int bankSize;
    
    public WordBank() {
        // Loads words into the "bank" field.
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/words/atomic_words.txt")))) {
            bank = reader.lines().collect(Collectors.toList());
            bankSize = bank.size();
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
        }
    }
    
    /**
     * This method generates and returns a list of words
     * from atomic_words.txt.
     *  @return a String of words, each separated by a space character.
     */
    public static String getWords() {
        StringBuilder words = new StringBuilder();
        Random rand = new Random();
        // Populates the List with random words from the "bank" field.
        for (int i = 0; i < 50; i++) {
            words.append(bank.get(rand.nextInt(bankSize)) + " ");
        }
        return words.toString();
    }
    
}
