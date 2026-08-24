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

/** @author Ethan Rocklin */

public class TxtExporter {
    
    private boolean accuracySelected;
    private boolean averageAccuracySelected;
    private boolean averageTypingScoreSelected;
    private boolean averageWPMSelected;
    private boolean dateSelected;
    private boolean formatSelected;
    private boolean timeElapsedSelected;
    private boolean wordCountsSelected;
    private boolean wpmSelected;
    private Instant now;
    private Path path;
    
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
        System.out.println(pathStr);
        Path path = Path.of(pathStr);
        this.path = path;
        export();
    }
    
    // This method executes all operations needed to construct and export the .txt file.
    private void export() {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            System.out.println("ERROR : File could not be created at path " + path.toString());
            e.printStackTrace();
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
            System.out.println("ERROR : Could not establish buffer for file writing");
            e.printStackTrace();
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
        str.append("Generated on " + date + "\n\n");
        try {
            writer.write(str.toString());
        } catch (IOException e) {
            System.out.println("ERROR : Could not write header to " + path);
            e.printStackTrace();
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
        try {
            writer.write(str.toString());
        } catch (IOException e) {
            System.out.println("ERROR : Could not write columns to " + path);
            e.printStackTrace();
        }
    }
    
    // This method handles the writing of the all rows of attempt data to the exported file.
    private void writeAttemptData(BufferedWriter writer) {
        
    }
    
    // This method handles the writing of the averages to the exported file.
    private void writeAverages(BufferedWriter writer) {
        
    }
    
}
