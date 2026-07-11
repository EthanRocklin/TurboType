package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * 
 * @author Ethan Rocklin
 */

public class TimedController {
    
    // Fields
    @FXML
    private TextField wordsField;
    @FXML
    private TextField typingField;
    private TimedSession session;
    
    /**
     * This method initializes the Timed Mode page after the page loads.
     */
    @FXML
    public void initialize() {
        session = new TimedSession();
        wordsField.setText(WordBank.getWords());
    }
    
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
    
    // Getters and Setters
    
    /**
     * This method returns the words the user has typed;
     * @return 
     */
    @FXML
    private String getTypedWords() {
        System.out.println(typingField.getText());
        return "";
    }
    
}
