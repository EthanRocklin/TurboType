package com.typingspeed.app;

import java.time.Instant;

/**
 * 
 * @author Ethan Rocklin
 */

public class AttemptManager {
    
    // CONSTANTS
    private final double MAX_WPM = 100;
    private final int ACCURACY_WEIGHT = 3;
    private final int WPM_WEIGHT = 10;
    private final String[] MESSAGES = {
        "Everyone starts somewhere. Don't be discouraged. Keep trying!",
        "Any progress is a success. Keep trying!",
        "One step forward... One step forward... Boom, more improvement!",
        "Keep the ball rolling!",
        "Now you're getting it. Look at those fingers go!",
        "You're on your way to greatness. Keep up the good work!",
        "Greatness called... It's wondering where you're at!",
        "Look how much you've improved. That's quite the spectacle!",
        "Just a little more accurate and you'll be bumped up!",
        "Just a little faster and you'll nail it!",
        "WOW! You're fast and accurate! Great job!"
    };
    
    // Fields
    private double accuracy;
    private int wpm, wordCount, correctWords;
    private long startTimeMilli, endTimeMilli;
    private boolean sentences;
    private char mode;
    
    // Constructors
    
    /**
     * This constructor is used to start and manager a new attempt upon the
     * first keystroke.
     * @param wordCount : the number of words to be typed
     * @param sentences : if the format of the attempt is full setences
     * @param mode : describes the mode the user is typing in
     */
    public AttemptManager(int wordCount, boolean sentences, char mode) {
        this.wordCount = wordCount;
        this.sentences = sentences;
        this.mode = mode;
        accuracy = 0;
        wpm = 0;
        correctWords = 0;
        startTimeMilli = Instant.now().toEpochMilli();
        endTimeMilli = 0;
    }
    
    // Calculates, sets, and returns the accuracy for the attempt.
    public double calculateAccuracy() {
        accuracy = ((double) correctWords / wordCount) * 100;
        return accuracy;
    }
    
    // Calculates, sets, and returns the Words Per Minute for the attempt.
    public int calculateWPM() {
        double minutes = (double) (endTimeMilli - startTimeMilli) / 60000;
        wpm = (int) (correctWords / minutes);
        return wpm;
    }
    
    // Getters
    
    // Caclulates and returns the stats message based on accuracy and WPM.
    public String getAttemptMessage() {
        // Accuracy (0-3) and WPM (0-10) scores, accuracy has less impact on final score
        double accuracyScore = (accuracy / 100) * ACCURACY_WEIGHT;
        // In case someone is fast enough to break the calculation
        if (wpm > MAX_WPM)
            wpm = 100;
        double wpmScore = (double) (wpm / MAX_WPM) * WPM_WEIGHT;
        // Converting to a final score from 0 - 10
        double finalScore = ((accuracyScore + wpmScore) / (ACCURACY_WEIGHT + WPM_WEIGHT)) * 10;
        // Messgae number from 0 to 10
        int messageNumber = (int) Math.floor(finalScore);
        return MESSAGES[messageNumber];
    }
    
    // Setters
    
    public void setCorrectWordCount(int count) {
        correctWords = count;
    }
    public void setEndTimeMilli() {
        endTimeMilli = Instant.now().toEpochMilli();
    }

}