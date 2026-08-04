package com.typingspeed.app;

import java.time.Instant;

/** @author Ethan Rocklin */

public class Attempt {
    
    // CONSTANTS
    private final int MAX_WPM = 100;
    private final int ACCURACY_WEIGHT = 3;
    private final int WPM_WEIGHT = 10;
    private final String[] MESSAGES = {
        "Everyone starts somewhere.\nDon't be discouraged.\nKeep trying!",
        "Any progress is a success.\nKeep trying!",
        "One step forward...\nOne step forward...\nBoom, more improvement!",
        "Keep the ball rolling!",
        "Now you're getting it.\nLook at those fingers go!",
        "You're on your way to greatness.\nKeep up the good work!",
        "Greatness called...\nIt's wondering where you're at!",
        "Look how much you've improved!\nThat's quite the spectacle!",
        "Just a little more accurate\nand you'll be bumped up!",
        "Just a little faster\nand you'll nail it!",
        "WOW!\nYou're fast and accurate!\nGreat job!"
    };
    
    // Fields
    private double accuracy;
    private int wpm, wordCount, correctWords, typingScore, attemptMilli;
    private long startTimeMilli, endTimeMilli;
    private boolean sentences;
    private String attemptTime;
    private char mode;
    
    // Constructors
    
    /**
     * This constructor is used to start and manager a new attempt upon the
     * first keystroke.
     * @param wordCount : the number of words to be typed
     * @param sentences : if the format of the attempt is full setences
     * @param mode : describes the mode the user is typing in
     */
    public Attempt(int wordCount, boolean sentences, char mode) {
        this.wordCount = wordCount;
        this.sentences = sentences;
        this.mode = mode;
        accuracy = 0;
        wpm = 0;
        correctWords = 0;
        startTimeMilli = Instant.now().toEpochMilli();
        endTimeMilli = 0;
    }
    
    // Calculates and sets the accuracy (2 decimal places) for the attempt.
    public void calculateAccuracy() {
        double rawAccuracy = ((double) correctWords / wordCount) * 100;
        accuracy = Math.round(rawAccuracy * 100) / 100.0;
    }
    
    // Calculates and sets the time it took to complete the attempt in MM:SS:MiMiMi format.
    public void calculateTime() {
        int totalMilli = (int) (endTimeMilli - startTimeMilli),
            milliseconds = totalMilli % 1000,
            seconds = totalMilli / 1000,
            minutes = seconds / 60;
        attemptMilli = totalMilli;
        // Storing String versions of time units for formatting
        String millisecondsStr = String.valueOf(milliseconds),
               secondsStr = String.valueOf(seconds),
               minutesStr = String.valueOf(minutes);
        attemptMilli = totalMilli;
        // Formatting
        if (milliseconds < 100) {
            millisecondsStr = "0" + millisecondsStr;
            if (milliseconds < 10) {
                millisecondsStr = "0" + millisecondsStr;
            }
        }
        if (seconds < 10) {
            secondsStr = "0" + secondsStr;
        }
        if (minutes < 10) {
            attemptTime = "0" + minutesStr + ":" + secondsStr + ":" + millisecondsStr;
        } else if (minutes >= 10 && minutes < 60) {
            attemptTime = minutesStr + ":" + secondsStr + ":" + millisecondsStr;
        } else {
            attemptTime = "59:59:999";
        }
    }
    
    // Calculates and sets the Typing Score for the attempt.
    public void calculateTypingScore() {
        // Accuracy (0-3) and WPM (0-10) scores, accuracy has less impact on final score
        double accuracyScore = (accuracy / 100) * ACCURACY_WEIGHT;
        // In case someone is fast enough to break the calculation
        if (wpm > MAX_WPM) {
            wpm = 100;
        }
        double wpmScore = (double) wpm / MAX_WPM * WPM_WEIGHT;
        // Converts the typing score to the range 0 - 10
        double rawScore = ((accuracyScore + wpmScore) / (ACCURACY_WEIGHT + WPM_WEIGHT)) * 10;
        typingScore = (int) Math.floor(rawScore);
    }
    
    // Calculates and sets the Words Per Minute for the attempt.
    public void calculateWPM() {
        double minutes = (double) (endTimeMilli - startTimeMilli) / 60000;
        wpm = (int) (correctWords / minutes);
    }
    
    // Getters
    
    // Returns the stats message based on the attempt's typing score.
    public double getAccuracy() {
        return accuracy;
    }
    public String getAttemptMessage() {
        return MESSAGES[typingScore];
    }
    public String getTime() {
        return attemptTime;
    }
    public int getMaxWPM() {
        return MAX_WPM;
    }
    public int getTypingScore() {
        return typingScore;
    }
    public int getWPM() {
        return wpm;
    }
    
    // Setters
    
    public void setCorrectWordCount(int count) {
        correctWords = count;
    }
    public void setEndTimeMilli() {
        endTimeMilli = Instant.now().toEpochMilli();
    }

}