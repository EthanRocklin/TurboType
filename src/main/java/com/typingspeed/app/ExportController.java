package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;

/** @author Ethan Rocklin */

public class ExportController {
    
    @FXML
    private CheckBox selectAllBox, accuracyBox, dateBox, timeElapsedBox,
                     wordCountsBox, wpmBox, averageAccuracyBox, 
                     averageTypingScoreBox, averageWPMBox;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField selectedDirectory;
    
    // FXML Handler Methods
    
    @FXML
    private void handleDocx() {
        System.out.println("Docx export handler connected");
    }
    
    @FXML
    private void handlePdf() {
        System.out.println("Pdf export handler connected");
    }
    
    @FXML
    private void handleTxt() {
        
    }
    
    @FXML
    private void handleXlsx() {
        System.out.println("Xlsx export handler connected");
    }
    
    // Other Methods
    
    @FXML
    private void selectAll() {
        if (selectAllBox.isSelected()) {
            accuracyBox.setSelected(true);
            dateBox.setSelected(true);
            timeElapsedBox.setSelected(true);
            wordCountsBox.setSelected(true);
            wpmBox.setSelected(true);
            averageAccuracyBox.setSelected(true);
            averageTypingScoreBox.setSelected(true);
            averageWPMBox.setSelected(true);
        } else {
            accuracyBox.setSelected(false);
            dateBox.setSelected(false);
            timeElapsedBox.setSelected(false);
            wordCountsBox.setSelected(false);
            wpmBox.setSelected(false);
            averageAccuracyBox.setSelected(false);
            averageTypingScoreBox.setSelected(false);
            averageWPMBox.setSelected(false);
        }
    }
    
    @FXML
    private void selectDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Export Folder");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // Links chooser dialog window to the parent stage
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        File directory = chooser.showDialog(stage);
        selectedDirectory.setText(directory.getAbsolutePath());
    }
    
}
