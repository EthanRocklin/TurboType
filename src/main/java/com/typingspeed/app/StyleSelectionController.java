package com.typingspeed.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

/** @author Ethan Rocklin */

public class StyleSelectionController {
    
    @FXML
    private CheckBox boldLabelsBox;
    @FXML
    private CheckBox coloredCellsBox;
    @FXML
    private CheckBox dataAlignmentBox;
    @FXML
    private CheckBox dataCenterAlignBox;
    @FXML
    private CheckBox dataLeftAlignBox;
    @FXML
    private CheckBox dataRightAlignBox;
    @FXML
    private CheckBox labelAlignmentBox;
    @FXML
    private CheckBox labelCenterAlignBox;
    @FXML
    private CheckBox labelLeftAlignBox;
    @FXML
    private CheckBox labelRightAlignBox;
    @FXML
    private CheckBox selectAllBox;
    
    private List<CheckBox> checkboxes;
    private List<CheckBox> options;
    
    @FXML
    public void initialize() {
        initiatizeCheckboxes();
    }
    
    // This method handles any initializing tasks for the checkboxes.
    
    // This method handles any initializing tasks for the checkboxes.
    private void initiatizeCheckboxes() {
        // Makes an iterateable list with the UI's checkboxes
        checkboxes = new ArrayList<>(Arrays.asList(
            boldLabelsBox,
            coloredCellsBox,
            dataAlignmentBox,
            dataCenterAlignBox,
            dataLeftAlignBox,
            dataRightAlignBox,
            labelAlignmentBox,
            labelCenterAlignBox,
            labelLeftAlignBox,
            labelRightAlignBox,
            selectAllBox
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

    // This method constructs and returns a record of the states of the model checkboxes.
    public StyleExportOptions getStyleOptions() {
        return new StyleExportOptions(
            boldLabelsBox.isSelected(),
            coloredCellsBox.isSelected(),
            dataAlignmentBox.isSelected(),
            dataCenterAlignBox.isSelected(),
            dataLeftAlignBox.isSelected(),
            dataRightAlignBox.isSelected(),
            labelCenterAlignBox.isSelected(),
            labelLeftAlignBox.isSelected(),
            labelRightAlignBox.isSelected(),
            selectAllBox.isSelected()
        );
    }
    
    // This method sets all style CheckBoxes to selected or not selected
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

}

