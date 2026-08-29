package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** @author Ethan Rocklin */

public class DataSelectionController {
    
    @FXML
    private CheckBox accuracyBox;
    @FXML
    private CheckBox averageAccuracyBox;
    @FXML
    private CheckBox averageTypingScoreBox;
    @FXML
    private CheckBox averageWpmBox;
    @FXML
    private CheckBox dateBox;
    @FXML
    private CheckBox formatBox;
    @FXML
    private CheckBox millisecondsBox;
    @FXML
    private CheckBox selectAllBox;
    @FXML
    private CheckBox timeElapsedBox;
    @FXML
    private CheckBox wordCountsBox;
    @FXML
    private CheckBox wpmBox;
    
    private List<CheckBox> checkboxes;
    
    @FXML
    public void initialize() {
        initiatizeCheckboxes();
    }
    
    // This method handles any initializing tasks for the checkboxes.
    private void initiatizeCheckboxes() {
        // Makes an iterateable list with the UI's checkboxes
        checkboxes = new ArrayList<>(Arrays.asList(
            accuracyBox,
            averageAccuracyBox,
            averageTypingScoreBox,
            averageWpmBox,
            dateBox,
            formatBox,
            millisecondsBox,
            selectAllBox,
            timeElapsedBox,
            wordCountsBox,
            wpmBox
        ));
        // Gets rid of focus visual when clicked
        for (CheckBox box : checkboxes) {
            box.setFocusTraversable(false);
        }
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
    
    // This method constructs and returns a record of the states of the data checkboxes.
    public DataExportOptions getDataOptions() {
        return new DataExportOptions(
            accuracyBox.isSelected(),
            averageAccuracyBox.isSelected(),
            averageTypingScoreBox.isSelected(),
            averageWpmBox.isSelected(),
            dateBox.isSelected(),
            formatBox.isSelected(),
            millisecondsBox.isSelected(),
            timeElapsedBox.isSelected(),
            wordCountsBox.isSelected(),
            wpmBox.isSelected()
        );
    }
    
}
