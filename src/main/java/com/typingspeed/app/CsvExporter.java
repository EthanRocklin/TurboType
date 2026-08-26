package com.typingspeed.app;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @author Ethan Rocklin */

public class CsvExporter {
    
    private static final Logger logger = Logger.getLogger(CsvExporter.class.getName());
    
    private final boolean accuracySelected;
    private final boolean averageAccuracySelected;
    private final boolean averageTypingScoreSelected;
    private final boolean averageWPMSelected;
    private final boolean dateSelected;
    private final boolean formatSelected;
    private final boolean timeElapsedSelected;
    private final boolean wordCountsSelected;
    private final boolean wpmSelected;
    private final Instant now;
    private List<String> dbAttributes;
    private final Path path;
    
    public CsvExporter(
            boolean accuracySelected,
            boolean averageAccuracySelected,
            boolean averageTypingScoreSelected,
            boolean averageWPMSelected,
            boolean dateSelected,
            boolean formatSelected,
            boolean timeElapsedSelected,
            boolean wordCountsSelected,
            boolean wpmSelected,
            String parentDirectory
    ) {
        this.accuracySelected = accuracySelected;
        this.averageAccuracySelected = averageAccuracySelected;
        this.averageTypingScoreSelected = averageTypingScoreSelected;
        this.averageWPMSelected = averageWPMSelected;
        this.dateSelected = dateSelected;
        this.formatSelected = formatSelected;
        this.timeElapsedSelected = timeElapsedSelected;
        this.wordCountsSelected = wordCountsSelected;
        this.wpmSelected = wpmSelected;
        this.now = Instant.now();
        this.dbAttributes = getAttributes();
        // Creates path for file to export to
        String pathStr = parentDirectory + File.separator + "typing_data_" + getTimeStamp() + ".csv";
        this.path = Path.of(pathStr);
        export();
    }
    
    // This method executes all operations needed to construct and export the .txt file.
    private void export() {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            logger.log(Level.WARNING, "File could not be created at path " + path.toString(), e);
        }
        // Creates a buffer to write to the file multiple times
        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writeColumns(writer);
            writeAttemptData(writer);
            writeAverages(writer);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not establish buffer for writing to " + path.toString(), e);
        }
    }
    
    // This method creates and returns a list of desired database attributes.
    private List<String> getAttributes() {
        List<String> attributes = new ArrayList<>();
        attributes.add("attempt_id");
        attributes.add("typing_score");
        // Determines which attributes to include
        if (wordCountsSelected) {
            attributes.add("total_words");
            attributes.add("correct_words");
        }
        if (accuracySelected) {
            attributes.add("accuracy");
        }
        if (wpmSelected) {
            attributes.add("wpm");
        }
        if (timeElapsedSelected) {
            attributes.add("time");
        }
        if (formatSelected) {
            attributes.add("format");
        }
        if (dateSelected) {
            attributes.add("date");
        }
        return attributes;
    }
    
    // This method gets and returns a String indicating the date and time of the export.
    private String getTimeStamp() {
        // Gets the clock time of the export
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
        String date = dateFormatter.format(now);
        // Gets the date of the export
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss").withZone(ZoneId.systemDefault());
        String time = timeFormatter.format(now);
        return date + "_" + time;
    }
    
    // This method handles the writing of the columns to the file.
    private void writeColumns(BufferedWriter writer) {
        StringBuilder str = new StringBuilder();
        str.append("Attempt");
        str.append(",Score");
        // Determines what columns to include
        if (wordCountsSelected) {
            str.append(",Words");
            str.append(",Correct");
        }
        if (accuracySelected) {
            str.append(",Accuracy");
        }
        if (wpmSelected) {
            str.append(",WPM");
        }
        if (timeElapsedSelected) {
            str.append(",Time");
        }
        if (formatSelected) {
            str.append(",Format");
        }
        if (dateSelected) {
            str.append(",Date");
        }
        str.append(System.lineSeparator());
        // Writes the built String to the file
        try {
            writer.write(str.toString());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not write columns to " + path.toString(), e);
        }
    }
    
    // This method handles the writing of the all rows of attempt data to the file.
    private void writeAttemptData(BufferedWriter writer) {
        StringBuilder query = new StringBuilder("SELECT ");
        // Adds all targeted attributes to the query
        for (int i = 0; i < dbAttributes.size() - 1; i++) {
            query.append(dbAttributes.get(i))
                 .append(",");
        }
        query.append(dbAttributes.getLast())
             .append(" from attempts");
        // Retrieves attempt data from the database
        ResultSet rs = null;
        String url = DatabaseManager.getURL();
        // Builds and writes attempt data (one line per attempt)
        try (Connection conn = DriverManager.getConnection(url)) {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(query.toString());
            // Checks if there is no attempt data
            if (!rs.next()) {
                return;
            }
            StringBuilder str = new StringBuilder();
            // Appends the first attempt's data
            for (int i = 0; i < dbAttributes.size() - 1; i++) {
                // All data is represented as a String for .csv export
                String datum = rs.getString(dbAttributes.get(i));
                str.append(datum)
                   .append(",");
            }
            str.append(dbAttributes.getLast())
               .append(System.lineSeparator());
            writer.write(str.toString());
            // Builds and writes if their is more data
            while (rs.next()) {
                str = new StringBuilder();
                for (int i = 0; i < dbAttributes.size() - 1; i++) {
                    String datum = rs.getString(dbAttributes.get(i));
                    str.append(datum)
                       .append(",");
                }
                String lastDatum = dbAttributes.getLast();
                str.append(rs.getString(lastDatum))
                   .append(System.lineSeparator());
                writer.write(str.toString());
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not write columns to " + path.toString(), e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to parse data from " + url, e);
        }
    }
    
    // This method handles the writing of the averages to the file.
    private void writeAverages(BufferedWriter writer) {
        try {
            double average = 0.0;
            writer.write("Averages");
            writer.write(System.lineSeparator());
            // Determines which averages to include
            if (averageAccuracySelected) {
                average = DatabaseManager.getAverageAccuracy();
                writer.write("Accuracy,");
                writer.write(String.valueOf(average));
                writer.newLine();
            }
            if (averageWPMSelected) {
                average = DatabaseManager.getAverageWPM();
                writer.write("Words Per Minute,");
                writer.write(String.valueOf(average));
                writer.newLine();
            }
            if (averageTypingScoreSelected) {
                average = DatabaseManager.getAverageTypingScore();
                writer.write("Typing Score,");
                writer.write(String.valueOf(average));
                writer.newLine();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not write averages to " + path.toString(), e);
        }
    }
    
}
