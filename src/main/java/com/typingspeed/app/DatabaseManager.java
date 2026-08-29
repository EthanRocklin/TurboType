package com.typingspeed.app;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @author Ethan Rocklin */

public class DatabaseManager {
    
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    
    final static String APP_DATA_DIR = System.getProperty("user.home") + File.separator + ".turbotype";
    final static String URL = "jdbc:sqlite:" + APP_DATA_DIR + File.separator + "user_data.db";
    
    // Utility class
    private DatabaseManager() {}

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
                logger.log(Level.WARNING, "Failed to connect to " + URL, e);
            }
            logger.log(Level.WARNING, "Failed to create datebase.", e);
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
        // Executes the query
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                accuracySum = rs.getDouble("accuracy_sum");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to retrieve average accuracy from " + URL, e);
        }
        // Rounds accuracy to the nearest hundredth
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
        // Executes the query
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                typingScoreSum = (double) rs.getInt("typing_score_sum");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to retrieve average typing score from " + URL, e);
        }
        // Rounds typing score to the nearest hundredth
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
        // Executes the query
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                wpmSum = (double) rs.getInt("wpm_sum");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to retrieve average words per minute from " + URL, e);
        }
        // Rounds WPM to the nearest hundredth
        return Math.round((wpmSum / attemptCount) * 100) / 100.0;
    }
    
    // This method gets the maximum value of the "attempt_id" attribute.
    private static int getMaxAttemptID() {
        int maxAttemptID = 0;
        String queryMaxAttemptID = "SELECT MAX(attempt_id) AS max_id FROM attempts";
        // Executes the query
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
            logger.log(Level.WARNING, "Failed to retrieve max attempt ID from " + URL, e);
        }
        return maxAttemptID;
    }
            
    // This method performs a SELECT query on the "attempts" table in the database.
    public static List<AttemptRecord> selectAttemptData(String query) {
        List<AttemptRecord> attempts = null;
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            // Checks if there is no attempt data
            if (!rs.next()) {
                return null;
            }
            // Builds list of attempt records
            attempts = new ArrayList<AttemptRecord>();
            ResultSetMetaData metaData = rs.getMetaData();
            Map<String, Boolean> cols = getColBooleans(metaData);
            do {
               // Creates an AttemptRecord for each row in the ResultSet
               attempts.add(getAttemptRecord(cols, rs));
            } while (rs.next());
            
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to parse data from " + URL, e);
        }
        return attempts;
    }
    
    // This method returns an AttemptRecord object for the current row of a ResultSet.
    private static AttemptRecord getAttemptRecord(Map<String, Boolean> colBooleans, ResultSet rs) {
        AttemptRecord record = null;
        try {
            // Assigns AttemptRecord values depending on what data the user selected
            Integer correctCount = colBooleans.get("correct_words") ? rs.getInt("correct_words") : null;
            Integer totalCount = colBooleans.get("total_words") ? rs.getInt("total_words") : null;
            Double accuracy = colBooleans.get("accuracy") ? rs.getDouble("accuracy") : null;
            Integer wpm = colBooleans.get("wpm") ? rs.getInt("wpm") : null;
            Integer milliseconds = colBooleans.get("milliseconds") ? rs.getInt("milliseconds") : null;
            String time = colBooleans.get("time") ? rs.getString("time") : null;
            String format = colBooleans.get("format") ? rs.getString("format") : null;
            String date = colBooleans.get("date") ? rs.getString("date") : null;
            record = new AttemptRecord(
                    rs.getInt("attempt_id"),
                    rs.getInt("typing_score"),
                    correctCount,
                    totalCount,
                    accuracy,
                    wpm,
                    milliseconds,
                    time,
                    format,
                    date
            );
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to parse data from " + URL, e);
        }
        return record;
    }
    
    // This method returns a boolean map for what columns exists in the user's selected data options.
    private static Map<String, Boolean> getColBooleans(ResultSetMetaData metaData) {
        Map<String, Boolean> colBooleans = new HashMap<>();
        try {
            Set<String> allColNames = getAllAttemptColNames();
            // Gets the select columns' names
            int colCount = metaData.getColumnCount();
            Set<String> colNames = new HashSet<>();
            for (int i = 1; i <= colCount; i++) {
                colNames.add(metaData.getColumnName(i));
            }
            // Adds column names to Map
            for (String name : allColNames) {
                colBooleans.put(name, (Boolean) colNames.contains(name));
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to parse data from " + URL, e);
        }
        return colBooleans;
    }
    
    // This method returns all column names for the "attempts" table in the database.
    private static Set<String> getAllAttemptColNames() {
        Set<String> colNames = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from attempts LIMIT 0");
            ResultSetMetaData metaData = rs.getMetaData();
            // Gets all columns names
            int colCount = metaData.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                colNames.add(metaData.getColumnName(i));
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to parse data from " + URL, e);
        }
        return colNames;
    }
    
    // This method returns a String representing the SELECT query for the user's data selection.
    public static String getSelectAttemptDataQuery(DataExportOptions dataOptions) {
        StringBuilder query = new StringBuilder("SELECT attempt_id, typing_score");
        if (dataOptions.wordCounts()) {
            query.append(", correct_words, total_words");
        }
        if (dataOptions.accuracy()) {
            query.append(", accuracy ");
        }
        if (dataOptions.wpm()) {
            query.append(", wpm ");
        }
        query.append(", milliseconds");
        if (dataOptions.timeElapsed()) {
            query.append(", time ");
        }
        if (dataOptions.format()) {
            query.append(", format ");
        }
        if (dataOptions.date()) {
            query.append(", date ");
        }
        query.append("from attempts");
        return query.toString();
    }
    
    public static String getAppDataDirectory() {
        return APP_DATA_DIR;
    }
    public static String getURL() {
        return URL;
    }
    
}
