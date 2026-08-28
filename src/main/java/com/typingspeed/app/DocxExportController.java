package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

/** @author Ethan Rocklin */

public class DocxExportController {
    
    @FXML
    private BrowseController browserController;
    @FXML
    private DataSelectionController dataSelectionController;
    @FXML
    private ModelSelectionController modelSelectionController;
    @FXML
    private DialogPane dialogPane;
    
    
    @FXML
    public void initialize() {
        initializeButtons();
    }
    
    // This method handles any intializing tasks for the dialog's buttons.
    private void initializeButtons() {
        Button exportButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        exportButton.setText("Export");
        exportButton.setOnAction(event -> initiateExporter());
    }
    
    @FXML
    private void initiateExporter() {
//        new DocxExporter(
//            
//        );
        System.out.println("DocxExporter functionality not defined");
    }
    
}
