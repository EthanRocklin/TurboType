package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.input.ScrollEvent;
import java.util.List;
import java.util.Arrays;

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
    @FXML
    private Text totalCounter;
    @FXML
    private Text correctCounter;
    private List<String> practiceWords;
    private List<String> typedWords;
    private PracticeSession session;
    
    /**
     * This method initializes the Practice Mode page after the page loads.
     */
    @FXML
    public void initialize() {
        session = new PracticeSession();
        // Populates text area with practice words
        String words = WordBank.getWords();
        wordsArea.setText(words);
        practiceWords = createWordList(words);
        
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
        
        // Sets counters
        totalCounter.setText(String.valueOf(practiceWords.size()));
        correctCounter.setText("0");
    }
    
    // Class Methods
    
    /**
     * This method takes a String of words that are delimited by spaces
     * and returns them as a list, with each index being one word.
     * @param str : A string consisting of one or more words.
     * @return a List object
     */
    private List<String> createWordList(String str) {
        String[] words = str.split(" ");
        return Arrays.asList(words);
    }
    
    // FXML Controller Methods
    
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
    
    // Checkers
    
    @FXML
    private void checkForSpace() {
        // TODO : Only check the typed words when the user types a space character
    }
    
    // Getters
    
    @FXML
    private String getTypedWords() {
        System.out.println(typingArea.getText());
        return "";
    }
    
    // Setters
    
    @FXML
    private void setCorrectWords() {
        
    }
    
    @FXML
    private void setTotalWords() {
        
    }
    
}
