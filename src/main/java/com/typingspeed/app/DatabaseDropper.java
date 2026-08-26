package com.typingspeed.app;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @author Ethan Rocklin */

public class DatabaseDropper {
    
    private static final Logger logger = Logger.getLogger(DatabaseDropper.class.getName());
    
    final static String APP_DATA_DIR = System.getProperty("user.home") + File.separator + ".turbotype";
    final static String URL = "jdbc:sqlite:" + APP_DATA_DIR + File.separator + "user_data.db";
    
    // ONLY MEANT FOR DEVELOPMENT
    public static void main(String[] args) {
        dropDatabase();
    }
    
    // Drops the database. ONLY USE FOR DEVELOPEMENT.
    private static void dropDatabase() {
        String dropAttempts = "DROP TABLE IF EXISTS attempts";
        boolean connectionFailed = true;
        try (Connection conn = DriverManager.getConnection(URL)) {
            connectionFailed = false;
            Statement stmt = conn.createStatement();
            // Drops tables
            stmt.execute(dropAttempts);
            logger.log(Level.INFO, "Successfully dropped database at {0}", URL);
        } catch (SQLException e) {
            if (connectionFailed) {
                logger.log(Level.WARNING, "Failed to connect to " + URL, e);
            }
            logger.log(Level.WARNING, "Failed to drop datebase.", e);
        }
    }
    
}
