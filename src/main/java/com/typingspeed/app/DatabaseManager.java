package com.typingspeed.app;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** @author Ethan Rocklin */

public class DatabaseManager {
    
    final static String APP_DATA_DIR = System.getProperty("user.home") + File.separator + ".turbotype";
    final static String URL = "jdbc:sqlite:" + APP_DATA_DIR + File.separator + "user_data.db";
    
    public static void main(String[] args) {
        createDatabase();
    }

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
    public static void dropDatabase() {
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
    
    public static String getURL() {
        return URL;
    }
    
}
