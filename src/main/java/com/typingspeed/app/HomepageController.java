package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * 
 * @author Ethan Rocklin
 */

public class HomepageController {
    
    @FXML
    private void displayPractice() throws IOException {
        App.setRoot("practice");
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
