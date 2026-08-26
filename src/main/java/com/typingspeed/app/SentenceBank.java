package com.typingspeed.app;

import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @author Ethan Rocklin */

public class SentenceBank {
    
    private static final Logger logger = Logger.getLogger(SentenceBank.class.getName());

    private static final List<String> BANK = loadSentences();
    private static final int BANK_SIZE = BANK.size();
    
    private SentenceBank() {}
    
    /**
     * This method generates and returns a list of sentences
     * from sentences.txt.
     *  @param count : the numnber of sentences to include
     *  @return a String of sentences
     */
    public static String getSentences(int count) {
        StringBuilder words = new StringBuilder();
        Random rand = new Random();
        // Populates the List with random words from the "bank" field.
        for (int i = 0; i < count; i++) {
            words.append(BANK.get(rand.nextInt(BANK_SIZE)))
                 .append(" ");
        }
        return words.toString();
    }
    
    // This method loads the sentences into memory upon class instantiation
    private static List<String> loadSentences() {
        List<String> sentences = null;
        // Loads sentences into the "bank" field.
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(SentenceBank.class.getResourceAsStream("/words/sentences.txt")))) {
            sentences = reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load sentences into memory.", e);
        }
        return sentences;
    }
    
}
