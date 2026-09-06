package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

/** @author Ethan Rocklin */

public class ModelSelectionController {
    
    @FXML
    private CheckBox barBox;
    @FXML
    private CheckBox barAccuracyFormatBox;
    @FXML
    private CheckBox barAccuracyTotalWordsBox;
    @FXML
    private CheckBox barScoreFormatBox;
    @FXML
    private CheckBox barScoreTotalWordsBox;
    @FXML
    private CheckBox barWpmFormatBox;
    @FXML
    private CheckBox barWpmTotalWordsBox;
    @FXML
    private CheckBox histoBox;
    @FXML
    private CheckBox histoAccuracyBox;
    @FXML
    private CheckBox histoCorrectWordsBox;
    @FXML
    private CheckBox histoScoreBox;
    @FXML
    private CheckBox histoWpmBox;
    @FXML
    private CheckBox lineBox;
    @FXML
    private CheckBox lineAccuracyBox;
    @FXML
    private CheckBox lineCorrectWordsBox;
    @FXML
    private CheckBox lineScoreBox;
    @FXML
    private CheckBox lineWpmBox;
    @FXML
    private CheckBox scatterBox;
    @FXML
    private CheckBox scatterGraphAccuracyBox;
    @FXML
    private CheckBox scatterGraphCorrectWordsBox;
    @FXML
    private CheckBox scatterGraphScoreBox;
    
    @FXML
    public void initialize() {
        initiatizeCheckboxes();
    }
    
    // This method handles any initializing tasks for the checkboxes.
    private void initiatizeCheckboxes() {
        // Gets rid of focus visual when clicked
        barBox.setFocusTraversable(false);
        barAccuracyFormatBox.setFocusTraversable(false);
        barAccuracyTotalWordsBox.setFocusTraversable(false);
        barScoreFormatBox.setFocusTraversable(false);
        barScoreTotalWordsBox.setFocusTraversable(false);
        barWpmFormatBox.setFocusTraversable(false);
        barWpmTotalWordsBox.setFocusTraversable(false);
        histoBox.setFocusTraversable(false);
        histoAccuracyBox.setFocusTraversable(false);
        histoCorrectWordsBox.setFocusTraversable(false);
        histoScoreBox.setFocusTraversable(false);
        histoWpmBox.setFocusTraversable(false);
        lineBox.setFocusTraversable(false);
        lineAccuracyBox.setFocusTraversable(false);
        lineCorrectWordsBox.setFocusTraversable(false);
        lineScoreBox.setFocusTraversable(false);
        lineWpmBox.setFocusTraversable(false);
        scatterBox.setFocusTraversable(false);
        scatterGraphAccuracyBox.setFocusTraversable(false);
        scatterGraphCorrectWordsBox.setFocusTraversable(false);
        scatterGraphScoreBox.setFocusTraversable(false);
    }
    
    // This method constructs and returns a record of the states of the model checkboxes.
    public ModelExportOptions getModelOptions() {
        return new ModelExportOptions(
            barAccuracyFormatBox.isSelected(),
            barAccuracyTotalWordsBox.isSelected(),
            barScoreFormatBox.isSelected(),
            barScoreTotalWordsBox.isSelected(),
            barWpmFormatBox.isSelected(),
            barWpmTotalWordsBox.isSelected(),
            histoAccuracyBox.isSelected(),
            histoCorrectWordsBox.isSelected(),
            histoScoreBox.isSelected(),
            histoWpmBox.isSelected(),
            lineAccuracyBox.isSelected(),
            lineCorrectWordsBox.isSelected(),
            lineScoreBox.isSelected(),
            lineWpmBox.isSelected(),
            scatterGraphAccuracyBox.isSelected(),
            scatterGraphCorrectWordsBox.isSelected(),
            scatterGraphScoreBox.isSelected()
        );
    }

}
