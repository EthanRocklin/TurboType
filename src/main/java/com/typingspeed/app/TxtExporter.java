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

public class TxtExporter {
    
    private static final Logger logger = Logger.getLogger(TxtExporter.class.getName());
    
    private final DataExportOptions dataOptions;
    private final Instant now;
    private final Path path;
    private final int COLUMN_WIDTH = 12;
    
    public TxtExporter(DataExportOptions dataOptions, String parentDirectory) {
        this.dataOptions = dataOptions;
        this.now = Instant.now();
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
    
    // This method gets and returns a String indicating the date and time of the export.
    private String getTimeStamp() {
        // Gets the clock time of the export
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault());
        String date = dateFormatter.format(now);
        // Gets the date of the export
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss")
                .withZone(ZoneId.systemDefault());
        String time = timeFormatter.format(now);
        return date + "_" + time;
    }
    
    // This method handles the writing of the header content to the file.
    private void writeHeader(BufferedWriter writer) {
        StringBuilder str = new StringBuilder("TurboType Data Report\n");
        str.append("--------------------------------\n");
        // Adds the date and time the report was generated
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-ss HH:mm:ss")
                .withZone(ZoneId.systemDefault());
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
        try {
            List<String> columnNames = getColumnNamesList();
            writer.write(getColumnNames(columnNames));
            int columnCount = columnNames.size();
            writer.write(getColumnHeaderLine(columnCount));
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not write columns to " + path.toString(), e);
        }
    }
    
    // This method builds and returns the column names as a String.
    private String getColumnNames(List<String> columnNames) {
        StringBuilder str = new StringBuilder();
        String column = "";
        String template = "%-" + COLUMN_WIDTH + "s";
        int columnCount = columnNames.size();
        for (int i = 0; i < columnCount; i++) {
            column = String.format(template, columnNames.get(i));
            str.append(column);
        }
        str.append(System.lineSeparator());
        return str.toString();
    }
    
    // This method builds and returns the line underneath the column names as a String.
    private String getColumnHeaderLine(int columnCount) {
        StringBuilder str = new StringBuilder();
        int hyphenCount = columnCount * COLUMN_WIDTH;
        for (int i = 1; i <= hyphenCount; i++) {
            str.append("-");
        }
        str.append(System.lineSeparator());
        return str.toString();
    }
    
    // This method constructs a list of the column names.
    private List<String> getColumnNamesList() {
        List<String> columnNames = new ArrayList<>();
        columnNames.add("Attempt");
        columnNames.add("Score");
        // Determines what columns to include
        if (dataOptions.wordCounts()) {
            columnNames.add("Correct");
            columnNames.add("Words");
        }
        if (dataOptions.accuracy()) {
            columnNames.add("Accuracy");
        }
        if (dataOptions.wpm()) {
            columnNames.add("WPM");
        }
        if (dataOptions.milliseconds()) {
            columnNames.add("Time (ms)");
        }
        if (dataOptions.timeElapsed()) {
            columnNames.add("Time");
        }
        if (dataOptions.format()) {
            columnNames.add("Format");
        }
        if (dataOptions.date()) {
            columnNames.add("Date");
        }
        return columnNames;
    }
    
    // This method handles the writing of the all rows of attempt data to the file.
    private void writeAttemptData(BufferedWriter writer) {
        String query = DatabaseManager.getSelectAttemptDataQuery(dataOptions);
        List<AttemptRecord> attempts = DatabaseManager.selectAttemptData(query);
        if (attempts != null) {
            try {
                String formatTemplate = "%-" + COLUMN_WIDTH + "s";
                StringBuilder str = new StringBuilder();
                // Builds and writes each attempt's data
                for (AttemptRecord record : attempts) {
                    String line = getDataLine(record); 
                    writer.write(line);
                }
                writer.newLine();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Could not write columns to " + path.toString(), e);
            } 
        }
    }
    
    // This method constructs and returns an attempt's data as a String.
    private String getDataLine(AttemptRecord record) {
        String template = "%-" + COLUMN_WIDTH + "s";
                StringBuilder line = new StringBuilder();
        String formatted = String.format(template, record.attemptId());
        line.append(formatted);
        formatted = String.format(template, record.typingScore());
        line.append(formatted);
        if (record.correctCount() != null) {
            formatted = String.format(template, record.correctCount());
            line.append(formatted);
        }
        if (record.totalCount() != null) {
            formatted = String.format(template, record.totalCount());
            line.append(formatted);
        }
        if (record.accuracy() != null) {
            formatted = String.format(template, record.accuracy());
            line.append(formatted);
        }
        if (record.wpm() != null) {
            formatted = String.format(template, record.wpm());
            line.append(formatted);
        }
        if (record.milliseconds() != null) {
            formatted = String.format(template, record.milliseconds());
            line.append(formatted);
        }
        if (record.time() != null) {
            formatted = String.format(template, record.time());
            line.append(formatted);
        }
        if (record.format() != null) {
            formatted = String.format(template, record.format());
            line.append(formatted);
        }
        if (record.date() != null) {
            formatted = String.format(template, record.date());
            line.append(formatted);
        }
        line.append(System.lineSeparator());
        return line.toString();
    }
    
    // This method handles the writing of the averages to the file.
    private void writeAverages(BufferedWriter writer) {
        StringBuilder str = new StringBuilder("Averages\n");
        str.append("--------\n");
        double average = 0.0;
        // Determines which averages to include
        if (dataOptions.averageAccuracy()) {
            average = DatabaseManager.getAverageAccuracy();
            str.append("Accuracy : ")
               .append(average)
               .append(System.lineSeparator());
        }
        if (dataOptions.averageWpm()) {
            average = DatabaseManager.getAverageWPM();
            str.append("Words Per Minute : ")
               .append(average)
               .append(System.lineSeparator());
        }
        if (dataOptions.averageTypingScore()) {
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
