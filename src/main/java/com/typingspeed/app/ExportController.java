package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;

/** @author Ethan Rocklin */

public class ExportController {
    
    @FXML
    private static DialogPane dialogPane;
    @FXML
    private static TextField selectedDirectory;
    
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
    private void selectDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Export Folder");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // Links chooser dialog window to the parent stage
        //Stage stage = (Stage) dialogPane.getScene().getWindow();
        File directory = chooser.showDialog(null);
        selectedDirectory.setText(directory.getAbsolutePath());
    }
    
    private void exportDocx() {
        
    }
    
}
