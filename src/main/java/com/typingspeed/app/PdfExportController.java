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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** @author Ethan Rocklin */

public class PdfExportController {
    
    @FXML
    private Button exportButton;
    @FXML
    private ButtonType okButton;
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
    
    private List<CheckBox> checkboxes;
    
    @FXML
    public void initialize() {
        initiatizeCheckboxes();
        initializeButtons();
    }
    
    // This method handles any intializing tasks for the dialog's buttons.
    private void initializeButtons() {
        Button exportButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        exportButton.setText("Export");
        exportButton.setOnAction(event -> initiateExporter());
    }
    
    // This method handles any initializing tasks for the checkboxes.
    private void initiatizeCheckboxes() {
        // Makes an iterateable list with the UI's checkboxes
        checkboxes = new ArrayList<CheckBox>(Arrays.asList(
            accuracyBox,
            averageAccuracyBox,
            averageTypingScoreBox,
            averageWPMBox,
            dateBox,
            formatBox,
            selectAllBox,
            timeElapsedBox,
            wordCountsBox,
            wpmBox
        ));
        // Applies target styles to checkboxes
        for (CheckBox box : checkboxes) {
            box.setFocusTraversable(false);
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
            for (CheckBox box : checkboxes) {
                box.setSelected(true);
            }
        } else {
            for (CheckBox box : checkboxes) {
                box.setSelected(false);
            }
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