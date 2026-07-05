package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;

public class StatsController {
    
    @FXML
    private void displayHomepage() throws IOException {
        App.setRoot("homepage");
    }
    
    @FXML
    private void displayPractice() throws IOException {
        App.setRoot("practice");
    } 
    
    @FXML
    private void displayTimed() throws IOException {
        App.setRoot("timed");
    }
    
}
