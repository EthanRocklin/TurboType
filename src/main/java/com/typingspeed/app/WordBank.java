package com.typingspeed.app;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class WordBank {

    private static List<String> words;
    
    public WordBank() {
        // Loads words into the "words" field.
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/basicWords.txt")))) {
            List<String> words = reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
        }
    }
    
    /**
     * This method <><><><>
     */
    private static List<String> getWordList() {
        List<String> words = new ArrayList<>(50);
        // Populates the List with 50 words.
        for (int i = 0; i < 50; i++) {
        // TODO <><><><>
            // String word = "";
        }
        return words;
    }
    
}
