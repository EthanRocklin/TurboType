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
    
    public CheckBox getAccuracyBox() {
        return accuracyBox;
    }
    public CheckBox getAverageAccuracyBox() {
        return averageAccuracyBox;
    }
    public CheckBox getAverageTypingScoreBox() {
        return averageTypingScoreBox;
    }
    public CheckBox getAverageWpmBox() {
        return averageWpmBox;
    }
    public CheckBox getDateBox() {
        return dateBox;
    }
    public CheckBox getFormatBox() {
        return formatBox;
    }
    public CheckBox getTimeElapsedBox() {
        return timeElapsedBox;
    }
    public CheckBox getWordCountsBox() {
        return wordCountsBox;
    }
    public CheckBox getWpmBox() {
        return wpmBox;
    }
    
}
