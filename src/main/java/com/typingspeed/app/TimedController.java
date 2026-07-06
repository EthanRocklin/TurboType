package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * 
 * @author Ethan Rocklin
 */

public class TimedController {
    
    @FXML
    private void displayHomepage() throws IOException {
        App.setRoot("homepage");
    }
    
    @FXML
    private void displayPractice() throws IOException {
        App.setRoot("practice");
    } 
    
    @FXML
    private void displayStats() throws IOException {
        App.setRoot("stats");
    }
    
}
