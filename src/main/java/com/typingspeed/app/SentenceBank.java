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

public class SentenceBank {

    private static List<String> bank;
    private static int bankSize;
    
    public SentenceBank() {
        // Loads sentences into the "bank" field.
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/words/atomic_words.txt")))) {
            bank = reader.lines().collect(Collectors.toList());
            bankSize = bank.size();
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
        }
    }
    
    /**
     * This method generates and returns a list of sentences
     * from sentences.txt.
     *  @return a String of sentences
     */
    public static String getSentences(int num) {
        StringBuilder words = new StringBuilder();
        Random rand = new Random();
        // Populates the List with random words from the "bank" field.
        for (int i = 0; i < num; i++) {
            words.append(bank.get(rand.nextInt(bankSize)) + " ");
        }
        return words.toString();
    }
    
}