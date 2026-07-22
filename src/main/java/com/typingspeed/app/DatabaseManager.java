package com.typingspeed.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Ethan Rocklin
 */
public class DatabaseManager {
    
    // Fields
    final static String URL = "jdbc:sqlite:turbotype.db";
    
    public static void main(String[] args) {
        createDatabase();
    }

    // Creates the database
    public static void createDatabase() {
        String createAttempts = """
                                CREATE TABLE IF NOT EXISTS attempts (
                                    attempt_id INTEGER PRIMARY KEY,
                                    seconds REAL NOT NULL,
                                    correct_words INTEGER NOT NULL,
                                    incorrect_words INTEGER NOT NULL
                                )
                                ;
                                """;
        boolean connectionFailed = true;
        try (Connection conn = DriverManager.getConnection(URL)) {
            System.out.print("> Connection to " + URL + " established\n");
            System.out.print("> Attempting to create the database...\n");
            connectionFailed = false;
            Statement stmt = conn.createStatement();
            // Creates tables
            stmt.execute(createAttempts);
            System.out.print("> Database was created");
        } catch (SQLException e) {
            if (connectionFailed) {
                System.out.print("ERROR: Failed to connect to " + URL + "\n");
            }
            System.out.print("ERROR: Failed to create database");
            e.printStackTrace();
            System.out.print(e.getMessage());
        }
                                
    }
    
    // Drops the database
    public static void dropDatabase() {
        String dropAttempts = "DROP TABLE IF EXISTS attemptsa";
        
        boolean connectionFailed = true;
        try (Connection conn = DriverManager.getConnection(URL)) {
            System.out.print("> Connection to " + URL + " established\n");
            System.out.print("> Attempting to drop the database...\n");
            connectionFailed = false;
            Statement stmt = conn.createStatement();
            // Drops tables
            stmt.execute(dropAttempts);
            System.out.print("> Database was dropped");
        } catch (SQLException e) {
            if (connectionFailed) {
                System.out.print("ERROR: Failed to connect to " + URL + "\n");
            }
            System.out.print("ERROR: Failed to drop database");
            e.printStackTrace();
            System.out.print(e.getMessage());
        }
    }
    
}
