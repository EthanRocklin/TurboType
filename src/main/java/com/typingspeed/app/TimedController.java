package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.ScrollEvent;

/**
 * 
 * @author Ethan Rocklin
 */

public class TimedController {
    
    // Fields
    @FXML
    private TextArea wordsArea;
    @FXML
    private TextArea typingArea;
    private TimedSession session;
    
    /**
     * This method initializes the Timed Mode page after the page loads.
     */
    @FXML
    public void initialize() {
        session = new TimedSession();
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
     * This method returns the words the user has typed.
     * @return 
     */
    @FXML
    private String getTypedWords() {
        return typingArea.getText();
    }
    
}
