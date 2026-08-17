package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * 
 * @author Ethan Rocklin
 */

public class StatsController {
    
    @FXML
    private void displayHomepage() throws IOException {
        TurboType.setRoot("homepage");
    }
    @FXML
    private void displayPractice() throws IOException {
        TurboType.setRoot("practice");
    } 
    @FXML
    private void displayTimed() throws IOException {
        TurboType.setRoot("timed");
    }
    
}
