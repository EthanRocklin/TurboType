package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.nio.file.Path;

/** @author Ethan Rocklin */

public class TxtExportController {
    
    @FXML
    private Button exportButton;
    @FXML
    private CheckBox accuracyBox;
    @FXML
    private CheckBox averageAccuracyBox;
    @FXML
    private CheckBox averageTypingScoreBox;
    @FXML
    private CheckBox averageWPMBox;
    @FXML
    private CheckBox dateBox;
    @FXML
    private CheckBox formatBox;
    @FXML
    private CheckBox selectAllBox;
    @FXML
    private CheckBox timeElapsedBox;
    @FXML
    private CheckBox wordCountsBox;
    @FXML
    private CheckBox wpmBox;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private TextField selectedDirectory;
    
    @FXML
    public void initialize() {
        Button exportButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        if (exportButton != null) {
            exportButton.setOnAction(event -> initiateExporter());
        }
    }
    
    @FXML
    private void initiateExporter() {
        new TxtExporter(
            accuracyBox.isSelected(),
            averageAccuracyBox.isSelected(),
            averageTypingScoreBox.isSelected(),
            averageWPMBox.isSelected(),
            dateBox.isSelected(),
            formatBox.isSelected(),
            timeElapsedBox.isSelected(),
            wordCountsBox.isSelected(),
            wpmBox.isSelected(),
            selectedDirectory.getText()
        );
    }
    
    @FXML
    private void selectAll() {
        if (selectAllBox.isSelected()) {
            accuracyBox.setSelected(true);
            dateBox.setSelected(true);
            formatBox.setSelected(true);
            timeElapsedBox.setSelected(true);
            wordCountsBox.setSelected(true);
            wpmBox.setSelected(true);
            averageAccuracyBox.setSelected(true);
            averageTypingScoreBox.setSelected(true);
            averageWPMBox.setSelected(true);
        } else {
            accuracyBox.setSelected(false);
            dateBox.setSelected(false);
            formatBox.setSelected(false);
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
