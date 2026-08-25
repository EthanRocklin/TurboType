package com.typingspeed.app;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

/** @author Ethan Rocklin */

public class DatabaseManager {
    
    final static String APP_DATA_DIR = System.getProperty("user.home") + File.separator + ".turbotype";
    final static String URL = "jdbc:sqlite:" + APP_DATA_DIR + File.separator + "user_data.db";
    
    // Utility class
    private DatabaseManager() {}

    // Creates the database
    public static void createDatabase() {
        // Creates the database if it does not exist
        File dir = new File(APP_DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String createAttempts = """
                                CREATE TABLE IF NOT EXISTS attempts (
                                    attempt_id INTEGER PRIMARY KEY NOT NULL,
                                    typing_score INTEGER NOT NULL,
                                    correct_words INTEGER NOT NULL,
                                    total_words INTEGER NOT NULL,
                                    accuracy REAL NOT NULL,
                                    wpm INTEGER NOT NULL,
                                    milliseconds INTEGER NOT NULL,
                                    time TEXT NOT NULL,
                                    format TEXT NOT NULL,
                                    date TEXT NOT NULL
                                );
                                """;
        boolean connectionFailed = true;
        try (Connection conn = DriverManager.getConnection(URL)) {
            connectionFailed = false;
            Statement stmt = conn.createStatement();
            // Creates tables
            stmt.execute(createAttempts);
        } catch (SQLException e) {
            if (connectionFailed) {
                System.out.print("ERROR: Failed to connect to " + URL + "\n");
            }
            System.out.print("ERROR: Failed to create database\n");
            e.printStackTrace();
            System.out.print(e.getMessage() + "\n");
        }
                                
    }
    
    // Drops the database
    private static void dropDatabase() {
        String dropAttempts = "DROP TABLE IF EXISTS attempts";
        boolean connectionFailed = true;
        try (Connection conn = DriverManager.getConnection(URL)) {
            connectionFailed = false;
            Statement stmt = conn.createStatement();
            // Drops tables
            stmt.execute(dropAttempts);
        } catch (SQLException e) {
            if (connectionFailed) {
                System.out.print("ERROR: Failed to connect to " + URL + "\n");
            }
            System.out.print("ERROR: Failed to drop database\n");
            e.printStackTrace();
            System.out.print(e.getMessage() + "\n");
        }
    }
    
    // This method gets the average accuracy using all attempt's stored in the database.
    public static double getAverageAccuracy() {
        int attemptCount = getMaxAttemptID();
        // If there are no attempts in the database 
        if (attemptCount == 0) {
            return 0.0;
        }
        // Gets the sum of the "accuracy" attribute
        double accuracySum = 0;
        String query = "SELECT SUM(accuracy) AS accuracy_sum FROM attempts";
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                accuracySum = rs.getDouble("accuracy_sum");
            }
        } catch (SQLException e) {
            System.out.print("ERROR: Failed to retrieve data from " + URL + "\n");
            e.printStackTrace();
        }
        // Rounds accuracy to the 
        return Math.round((accuracySum / attemptCount) * 100) / 100.0;
    }
    
    // This method gets the average typing score using all attempt's stored in the database.
    public static double getAverageTypingScore() {
        int attemptCount = getMaxAttemptID();
        // If there are no attempts in the database
        if (attemptCount == 0) {
            return 0.0;
        }
        // Gets the sum of the "typing_score" attribute
        double typingScoreSum = 0.0;
        String query = "SELECT SUM(typing_score) AS typing_score_sum FROM attempts";
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                typingScoreSum = (double) rs.getInt("typing_score_sum");
            }
        } catch (SQLException e) {
            System.out.print("ERROR: Failed to retrieve data from " + URL + "\n");
            e.printStackTrace();   
        }
        return Math.round((typingScoreSum / attemptCount) * 100) / 100.0;
    }
    
    // This method gets the average words per minute using all attempt's stored in the database.
    public static double getAverageWPM() {
        int attemptCount = getMaxAttemptID();
        // If there are no attempts in the database
        if (attemptCount == 0) {
            return 0.0;
        }
        // Gets the sum of the "wpm" attribute
        double wpmSum = 0.0;
        String query = "SELECT SUM(wpm) AS wpm_sum FROM attempts";
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                wpmSum = (double) rs.getInt("wpm_sum");
            }
        } catch (SQLException e) {
            System.out.print("ERROR: Failed to retrieve data from " + URL + "\n");
            e.printStackTrace();   
        }
        return Math.round((wpmSum / attemptCount) * 100) / 100.0;
    }
    
    // This method gets the maximum value of the "attempt_id" attribute.
    private static int getMaxAttemptID() {
        int maxAttemptID = 0;
        String queryMaxAttemptID = "SELECT MAX(attempt_id) AS max_id FROM attempts";
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryMaxAttemptID);
            if (rs.next()) {
                maxAttemptID = rs.getInt("max_id");
                if (rs.wasNull()) {
                    // If there are not attempts in the database 
                    return 0;
                }
            }
        } catch (SQLException e) {
            System.out.print("ERROR: Failed to retrieve data from " + URL + "\n");
            e.printStackTrace();
        }
        return maxAttemptID;
    }
    
    public static String getURL() {
        return URL;
    }
    
}
