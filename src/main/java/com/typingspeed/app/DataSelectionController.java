package com.typingspeed.app;

import javafx.event.ActionEvent;
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
    private List<CheckBox> options;
    
    @FXML
    public void initialize() {
        initiatizeCheckboxes();
    }
    
    // This method handles any initializing tasks for the checkboxes.
    private void initiatizeCheckboxes() {
        // Makes an iterateable list with the data selection checkboxes
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
        options = new ArrayList<>(checkboxes);
        options.remove(selectAllBox);
        // Gets rid of focus visual when clicked
        for (CheckBox box : checkboxes) {
            box.setFocusTraversable(false);
        }
    }
    
    // This method updates the selectAll box's select status for edge cases.
    @FXML
    private void checkSelectAll(ActionEvent event) {
        CheckBox box = (CheckBox) event.getSource();
        // The user deselected an option after clicking Select All
        if (!box.isSelected() && selectAllBox.isSelected()) {
            selectAllBox.setSelected(false);
            return;
        }
        // The user selected all options without clicking Select All
        if (!selectAllBox.isSelected()) {
            for (CheckBox option : options) {
                if (!option.isSelected()) {
                    return;
                }
            }
            selectAllBox.setSelected(true);
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
