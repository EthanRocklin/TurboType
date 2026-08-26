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
    private final Path path;
    
    public TxtExporter(
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
        now = Instant.now();
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
            logger.log(Level.WARNING, "ERROR : Could not establish buffer for writing to " + path.toString(), e);
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
    
    // This method handles the writing of the header content to the exported file.
    private void writeHeader(BufferedWriter writer) {
        StringBuilder str = new StringBuilder("TurboType Data Report\n");
        str.append("---------------------\n");
        // Adds the date and time the report was generated
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-ss HH:mm:ss").withZone(ZoneId.systemDefault());
        String date = dateFormatter.format(now);
        str.append("Generated on ")
           .append(date)
           .append(System.lineSeparator())
           .append(System.lineSeparator());
        try {
            writer.write(str.toString());
        } catch (IOException e) {
            logger.log(Level.WARNING, "ERROR : Could not write header to " + path.toString(), e);
        }
    }

    // INCOMPLETE
    // This method handles the writing of the columns to the exported file.
    private void writeColumns(BufferedWriter writer) {
        StringBuilder str = new StringBuilder();
        str.append("Attempt #\t");
        str.append("Typing Score");
        if (wordCountsSelected) {
            str.append("\tWords");
            str.append("\tCorrect");
        }
        if (accuracySelected) {
            str.append("\tAccuracy");
        }
        if (wpmSelected) {
            str.append("\tWPM");
        }
        if (timeElapsedSelected) {
            str.append("\tTime");
        }
        if (formatSelected) {
            str.append("\tFormat");
        }
        if (dateSelected) {
            str.append("\tDate");
        }
        str.append(System.lineSeparator());
        try {
            writer.write(str.toString());
        } catch (IOException e) {
            logger.log(Level.WARNING, "ERROR : Could not write columns to " + path.toString(), e);
        }
    }
    
    // This method handles the writing of the all rows of attempt data to the exported file.
    private void writeAttemptData(BufferedWriter writer) {
        
    }
    
    // This method handles the writing of the averages to the exported file.
    private void writeAverages(BufferedWriter writer) {
        StringBuilder str = new StringBuilder("Averages\n");
        str.append("--------\n");
        double average = 0.0;
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
        try {
            writer.write(str.toString());
        } catch (IOException e) {
            logger.log(Level.WARNING, "ERROR : Could not write averages to " + path.toString(), e);
        }
    }
    
}
