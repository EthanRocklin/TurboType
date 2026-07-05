package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;

public class PracticeController {
    
    @FXML
    private void displayHomepage() throws IOException {
        App.setRoot("homepage");
    }
    
    @FXML
    private void displayTimed() throws IOException {
        App.setRoot("timed");
    } 
    
    @FXML
    private void displayStats() throws IOException {
        App.setRoot("stats");
    }
    
}
