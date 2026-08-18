package com.typingspeed.app;

import javafx.fxml.FXML;

/** @author Ethan Rocklin */

public class ExportController {
    
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
        System.out.println("Txt export handler connected");
    }
    
    @FXML
    private void handleXlsx() {
        System.out.println("Xlsx export handler connected");
    }
    
    // Other Methods
    
    private void export() {
        
    }
    
}
