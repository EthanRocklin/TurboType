package com.typingspeed.app;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
    
    private final DataExportOptions dataOptions;
    private final Instant now;
    private final Path path;
    
    public CsvExporter(DataExportOptions dataOptions, String parentDirectory) {
        this.dataOptions = dataOptions;
        this.now = Instant.now();
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
        try {
            writer.write(getColumnHeaders());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not write columns to " + path.toString(), e);
        }
    }
    
    // This method builds and returns the column headers as a String.
    private String getColumnHeaders() {
        StringBuilder str = new StringBuilder();
        str.append("Attempt");
        str.append(",Score");
        // Determines what columns to include
        if (dataOptions.wordCounts()) {
            str.append(",Words");
            str.append(",Correct");
        }
        if (dataOptions.accuracy()) {
            str.append(",Accuracy");
        }
        if (dataOptions.wpm()) {
            str.append(",WPM");
        }
        if (dataOptions.milliseconds()) {
            str.append(",Milliseconds");
        }
        if (dataOptions.timeElapsed()) {
            str.append(",Time");
        }
        if (dataOptions.format()) {
            str.append(",Format");
        }
        if (dataOptions.date()) {
            str.append(",Date");
        }
        str.append(System.lineSeparator());
        return str.toString();
    }
    
    // This method handles the writing of the all rows of attempt data to the file.
    private void writeAttemptData(BufferedWriter writer) {
        String query = DatabaseManager.getSelectAttemptDataQuery(dataOptions);
        List<AttemptRecord> attempts = DatabaseManager.selectAttemptData(query);
        if (attempts != null) {
            try {
                // Builds and writes each attempt's data
                for (AttemptRecord record : attempts) {
                    String line = getDataLine(record); 
                    writer.write(line);
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not write columns to " + path.toString(), e);
            } 
        }
    }
    
    // This method constructs and returns an attempt's data as a String.
    private String getDataLine(AttemptRecord record) {
        StringBuilder line = new StringBuilder();
        line.append(record.attemptId())
            .append(",")
            .append(record.typingScore());
        if (record.correctCount() != null) {
            line.append(",")
                .append(record.correctCount());
        }
        if (record.totalCount() != null) {
            line.append(",")
                .append(record.totalCount());
        }
        if (record.accuracy() != null) {
            line.append(",")
                .append(record.accuracy());
        }
        if (record.wpm() != null) {
            line.append(",")
                .append(record.wpm());
        }
        if (record.milliseconds() != null) {
            line.append(",")
                .append(record.milliseconds());
        }
        if (record.time() != null) {
            line.append(",")
                .append(record.time());
        }
        if (record.format() != null) {
            line.append(",")
                .append(record.format());
        }
        if (record.date() != null) {
            line.append(",")
                .append(record.date());
        }
        line.append(System.lineSeparator());
        return line.toString();
    }
    
    // This method handles the writing of the averages to the file.
    private void writeAverages(BufferedWriter writer) {
        try {
            double average = 0.0;
            writer.write("Averages");
            writer.write(System.lineSeparator());
            // Determines which averages to include
            if (dataOptions.averageAccuracy()) {
                average = DatabaseManager.getAverageAccuracy();
                writer.write("Accuracy,");
                writer.write(String.valueOf(average));
                writer.newLine();
            }
            if (dataOptions.averageWpm()) {
                average = DatabaseManager.getAverageWPM();
                writer.write("Words Per Minute,");
                writer.write(String.valueOf(average));
                writer.newLine();
            }
            if (dataOptions.averageTypingScore()) {
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
