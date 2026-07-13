package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.ScrollEvent;

/**
 * 
 * @author Ethan Rocklin
 */

public class PracticeController {
    
    // Fields
    @FXML
    private TextArea wordsArea;
    @FXML
    private TextArea typingArea;
    private PracticeSession session;
    
    /**
     * This method initializes the Practice Mode page after the page loads.
     */
    @FXML
    public void initialize() {
        session = new PracticeSession();
        wordsArea.setText(WordBank.getWords());
        
        // Along with related XML, this disables scrolling in the text areas.
        wordsArea.addEventFilter(ScrollEvent.SCROLL, ScrollEvent::consume);
        typingArea.addEventFilter(ScrollEvent.SCROLL, ScrollEvent::consume);
        
        // Binds proportional sizing to the text areas
        wordsArea.sceneProperty().addListener((obs, oldScene, newScene) -> {
           if (newScene != null) {
               wordsArea.prefWidthProperty().bind(newScene.widthProperty().multiply(0.5));
               wordsArea.prefHeightProperty().bind(newScene.heightProperty().multiply(0.2));
               typingArea.prefWidthProperty().bind(newScene.widthProperty().multiply(0.5));
               typingArea.prefHeightProperty().bind(newScene.heightProperty().multiply(0.2));
           } 
        });
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
        System.out.println(typingArea.getText());
        return "";
    }
    
}
