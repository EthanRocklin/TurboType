package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;

/** @author Ethan Rocklin */

public class HomepageController {
    
    @FXML
    private void displayCredits() throws IOException {
        TurboType.setRoot("credits");
    }
    @FXML
    private void displayPractice() throws IOException {
        TurboType.setRoot("practice");
    }
    @FXML
    private void displayTimed() throws IOException {
        TurboType.setRoot("timed");
    }
    @FXML
    private void displayStats() throws IOException {
        TurboType.setRoot("stats");
    }
}
