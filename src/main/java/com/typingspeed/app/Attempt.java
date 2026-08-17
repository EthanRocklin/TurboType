package com.typingspeed.app;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/** @author Ethan Rocklin */

public class Attempt {
    
    private final int MAX_WPM = 100;
    private final int ACCURACY_WEIGHT = 3;
    private final int WPM_WEIGHT = 10;
    private final String[] MESSAGES = {
        "Everyone starts somewhere.\nDon't be discouraged.\nKeep trying!",
        "Any progress is a success.\nKeep trying!",
        "One step forward...\nOne step forward...\nBoom, more improvement!",
        "Keep the ball rolling!",
        "Now you're getting it.\nLook at those fingers go!",
        "You're on the way to greatness.\nKeep up the good work!",
        "Greatness called...\nIt's wondering where you're at!",
        "Look how much you've improved!\nThat's quite the spectacle!",
        "Just a little more accurate\nand you'll be bumped up!",
        "Just a little faster\nand you'll nail it!",
        "WOW!\nYou're fast and accurate!\nGreat job!"
    };
    
    private double accuracy;
    private int wpm, wordCount, correctCount, typingScore, attemptMilli;
    private long startTimeMilli, endTimeMilli;
    private boolean sentences, isTimed;
    private String attemptTime;
    
    // Constructors
    
    /**
     * This constructor is used to start and manager a new attempt upon the
     * first keystroke.
     * @param wordCount : the number of words to be typed
     * @param sentences : if the format of the attempt is full setences
     * @param mode : describes the mode the user is typing in
     */
    public Attempt(int wordCount, boolean sentences, boolean isTimed) {
        this.wordCount = wordCount;
        this.sentences = sentences;
        this.isTimed = isTimed;
        accuracy = 0;
        wpm = 0;
        correctCount = 0;
        startTimeMilli = Instant.now().toEpochMilli();
        endTimeMilli = 0;
    }
    
    // Calculates and sets the accuracy (2 decimal places) for the attempt.
    public void calculateAccuracy() {
        double rawAccuracy = ((double) correctCount / wordCount) * 100;
        accuracy = Math.round(rawAccuracy * 100) / 100.0;
    }
    
    // Calculates and sets the time it took to complete the attempt in MM:SS:MiMiMi format.
    public void calculateTime() {
        int totalMilli = (int) (endTimeMilli - startTimeMilli);
        int milliseconds = totalMilli % 1000;
        int rawSeconds = totalMilli / 1000;
        int seconds = rawSeconds % 60;
        int minutes = rawSeconds / 60;
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
        wpm = (int) (correctCount / minutes);
    }
    
    // Saves attempt data to the database as a single row in "attempts" table.
    public void saveAttempt() {
        boolean connectionFailed = true;
        // Gets a formated String for the date of the attempt
        Instant date = Instant.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
        String timeStamp = dateFormatter.format(date);
        // Prepares variables and INSERT statement
        String url = DatabaseManager.getURL();
        String format = "";
        if (sentences) {
            format = "sentences";
        } else {
            format = "random";
        }
        String insert = """
                      INSERT INTO attempts
                      (typing_score, correct_words, total_words, accuracy, wpm, 
                       milliseconds, time, format, date)
                      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                      """;
        // Saves attempt data to the user's local database
        try (Connection conn = DriverManager.getConnection(url)) {
            connectionFailed = false;
            // Inserting attempt data into insertion parameters
            PreparedStatement pstmt = conn.prepareStatement(insert);
            pstmt.setInt(1, typingScore);
            pstmt.setInt(2, correctCount);
            pstmt.setInt(3, wordCount);
            pstmt.setDouble(4, accuracy);
            pstmt.setInt(5, wpm);
            pstmt.setInt(6, attemptMilli);
            pstmt.setString(7, attemptTime);
            pstmt.setString(8, format);
            pstmt.setString(9, timeStamp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (connectionFailed) {
                System.out.print("ERROR: Failed to connect to " + url + "\n");
            }
            System.out.print("ERROR: Failed to save attempt\n");
            e.printStackTrace();
            System.out.print(e.getMessage() + "\n");
        }
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
    public boolean getIsTimed() {
        return isTimed;
    }
    public int getTypingScore() {
        return typingScore;
    }
    public int getWPM() {
        return wpm;
    }
    
    // Setters
    
    public void setCorrectWordCount(int count) {
        correctCount = count;
    }
    public void setEndTimeMilli() {
        endTimeMilli = Instant.now().toEpochMilli();
    }

}