package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Spinner;
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
    private Text correctCounter;
    @FXML
    private TextArea wordsArea;
    @FXML
    private TextArea typingArea;
    @FXML
    private Text totalCounter;
    @FXML
    private ToggleButton sentencesToggle;
    @FXML
    private Spinner<Integer> sentenceCounter;
    
    private List<String> practiceWords;
    private List<String> typedWords;
    
    /**
     * This method initializes the Practice Mode page after the page loads.
     */
    @FXML
    public void initialize() {
        // Populates text area with practice words
        String words = WordBank.getWords(30);
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
        // Sets Spinner values
        int min = 1, max = 6, initial = 3;
        sentenceCounter = new Spinner<>(min, max, initial);
        sentenceCounter.getValueFactory().setValue(initial);
        sentenceCounter.setVisible(false);
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
    
    @FXML
    private void togglePracticeWords() {
        String words = "";
        int count = 0;
        // If "Sentences" is selected
        if (sentencesToggle.isSelected()) {
            count = 3;
            updatePracticeWords(count, true);
            sentencesToggle.setText("Sentences");
            sentenceCounter.setVisible(true);
        } else {
            count = 30;
            updatePracticeWords(count, false);
            sentencesToggle.setText("Random");
            sentenceCounter.setVisible(false);
        }
    }
    
    /**
     * 
     * @param count : 
     * @param sentences : 
     */
    private void updatePracticeWords(int count, boolean sentences) {
        String words = "";
        if (sentences)
            words = SentenceBank.getSentences(count);
        else
            words = WordBank.getWords(count);
        practiceWords = createWordList(words);
        wordsArea.setText(words);
        setTotalWordsCounter();
    }
    
    @FXML
    private void updateSentenceCount() {
        updatePracticeWords(sentenceCounter.getValue(), true);
    }
    
    // Checkers
    
    @FXML
    private void checkTypedWords() {
        int correctWords = 0;
        String typedContent = typingArea.getText();
        String[] words = typedContent.split(" ");
        typedWords = Arrays.asList(words);
        // Checks typed words against practice words
        for (int i = 0; i < typedWords.size(); i++) {
            String practiceWord = practiceWords.get(i);
            String typedWord = typedWords.get(i);
            if (practiceWord.equals(typedWord)) {
                correctWords++;
            }
        }
        correctCounter.setText(String.valueOf(correctWords));
    }
    
    // Getters
    
    @FXML 
    private int getSentenceCount() {
        return sentenceCounter.getValue();
    }
    
    @FXML
    private String getTypedWords() {
        System.out.println(typingArea.getText());
        return "";
    }
    
    // Setters
    
    @FXML
    private void setTotalWordsCounter() {
        String count = String.valueOf(practiceWords.size());
        totalCounter.setText(count);
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
    
}