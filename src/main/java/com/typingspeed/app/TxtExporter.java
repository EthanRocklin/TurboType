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

public class TxtExporter {
    
    private static final Logger logger = Logger.getLogger(TxtExporter.class.getName());
    
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
    private final int COLUMN_WIDTH = 12;
    private List<String> dbAttributes;
    private final Path path;
    
    public TxtExporter(
            DataExportOptions dataOptions,
            String parentDirectory
    ) {
        this.accuracySelected = dataOptions.accuracy();
        this.averageAccuracySelected = dataOptions.averageAccuracy();
        this.averageTypingScoreSelected = dataOptions.averageTypingScore();
        this.averageWPMSelected = dataOptions.averageWpm();
        this.dateSelected = dataOptions.date();
        this.formatSelected = dataOptions.format();
        this.timeElapsedSelected = dataOptions.timeElapsed();
        this.wordCountsSelected = dataOptions.wordCounts();
        this.wpmSelected = dataOptions.wpm();
        this.now = Instant.now();
        this.dbAttributes = getAttributes();
        // Creates path for file to export to
        String pathStr = parentDirectory + File.separator + "typing_data_" + getTimeStamp() + ".txt";
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
            writeHeader(writer);
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
    
    // This method handles the writing of the header content to the file.
    private void writeHeader(BufferedWriter writer) {
        StringBuilder str = new StringBuilder("TurboType Data Report\n");
        str.append("--------------------------------\n");
        // Adds the date and time the report was generated
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-ss HH:mm:ss").withZone(ZoneId.systemDefault());
        String date = dateFormatter.format(now);
        str.append("Generated on ")
           .append(date);
        // Writes the built String to the file
        try {
            writer.write(str.toString());
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not write header to " + path.toString(), e);
        }
    }

    // This method handles the writing of the columns to the file.
    private void writeColumns(BufferedWriter writer) {
        List<String> columnNames = new ArrayList<>();
        StringBuilder str = new StringBuilder();
        columnNames.add("Attempt");
        columnNames.add("Score");
        // Determines what columns to include
        if (wordCountsSelected) {
            columnNames.add("Words");
            columnNames.add("Correct");
        }
        if (accuracySelected) {
            columnNames.add("Accuracy");
        }
        if (wpmSelected) {
            columnNames.add("WPM");
        }
        if (timeElapsedSelected) {
            columnNames.add("Time");
        }
        if (formatSelected) {
            columnNames.add("Format");
        }
        if (dateSelected) {
            columnNames.add("Date");
        }
        // Builds the columns header with dynamic spacing
        String column = "";
        String template = "%-" + COLUMN_WIDTH + "s";
        int columnCount = columnNames.size();
        for (int i = 0; i < columnCount; i++) {
            column = String.format(template, columnNames.get(i));
            str.append(column);
        }
        str.append(System.lineSeparator());
        // Builds line under the columns header with dynamic spacing
        String lastColumn = columnNames.getLast();
        int hyphenCount = columnCount * COLUMN_WIDTH;
        for (int i = 1; i <= hyphenCount; i++) {
            str.append("-");
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
            String formatTemplate = "%-" + COLUMN_WIDTH + "s";
            StringBuilder str = new StringBuilder();
            // Appends the first attempt's data
            for (String attribute : dbAttributes) {
                // All data is represented as a String for .txt export
                String datum = rs.getString(attribute);
                String formatted = String.format(formatTemplate, datum);
                str.append(formatted);
            }
            str.append(System.lineSeparator());
            writer.write(str.toString());
            // Builds and writes if their is more data
            while (rs.next()) {
                str = new StringBuilder();
                for (String attribute : dbAttributes) {
                    String datum = rs.getString(attribute);
                    String formatted = String.format(formatTemplate, datum);
                    str.append(formatted);
                }
                str.append(System.lineSeparator());
                writer.write(str.toString());
            }
            writer.newLine();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not write columns to " + path.toString(), e);
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to parse data from " + url, e);
        }
    }
    
    // This method handles the writing of the averages to the file.
    private void writeAverages(BufferedWriter writer) {
        StringBuilder str = new StringBuilder("Averages\n");
        str.append("--------\n");
        double average = 0.0;
        // Determines which averages to include
        if (averageAccuracySelected) {
            average = DatabaseManager.getAverageAccuracy();
            str.append("Accuracy : ")
               .append(average)
               .append(System.lineSeparator());
        }
        if (averageWPMSelected) {
            average = DatabaseManager.getAverageWPM();
            str.append("Words Per Minute : ")
               .append(average)
               .append(System.lineSeparator());
        }
        if (averageTypingScoreSelected) {
            average = DatabaseManager.getAverageTypingScore();
            str.append("Typing Score : ")
               .append(average)
               .append(System.lineSeparator());
        }
        // Writes the built String to the file
        try {
            writer.write(str.toString());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not write averages to " + path.toString(), e);
        }
    }
    
}
