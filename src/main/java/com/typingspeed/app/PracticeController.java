package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.util.List;

/**
 * 
 * @author Ethan Rocklin
 */

public class PracticeController {
    
    // Fields
    @FXML
    private TextField wordsField;
    @FXML
    private TextField typingField;
    private PracticeSession session;
    
    /**
     * This method initializes the Practice Mode page after the page loads.
     */
    @FXML
    public void initialize() {
        session = new PracticeSession();
        wordsField.setText(WordBank.getWords());
    }
    
    /**
     * This method deloads the Practice Mode page and loads the hoempage.
     * @throws IOException 
     */
    @FXML
    private void displayHomepage() throws IOException {
        App.setRoot("homepage");
    }
    
    /**
     * This method deloads the Practice Mode page and loads the Timed Type page.
     * @throws IOException 
     */
    @FXML
    private void displayTimed() throws IOException {
        App.setRoot("timed");
    } 
    
    /**
     * This method deloads the Practice Mode page and loads the Statistics page.
     * @throws IOException 
     */
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
